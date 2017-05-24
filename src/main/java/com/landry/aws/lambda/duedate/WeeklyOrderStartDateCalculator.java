package com.landry.aws.lambda.duedate;

import org.joda.time.DateTime;

import com.landry.aws.lambda.common.util.MyDateUtil;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBean;

public class WeeklyOrderStartDateCalculator
{

	private VendorShipTimeDataBean vendorShipTime;
	private DateTime startDate;

	public DateTime getStartDate()
	{
		/*
		 * TODO: Need to check if the orderDate you find falls on a holiday. If
		 * so need to get the next order date that does not fall on a holiday
		 * 
		 */

		/*
		 * First check if today is one of the regular order days. and if so
		 * check if past cutOff time to see if we need to do more calculation.
		 */

		for (Integer orderDay : vendorShipTime.getOrderDays())
			if (startDate.getDayOfWeek() == orderDay)
				if (!MyDateUtil.isPastCutOffTime(vendorShipTime.getCutOffTime()))
					return startDate;
		/*
		 * If made it here startDate is NOT on an order day OR we are past the cutOff
		 * time on an order day.
		 */
		return findNextOrderDay();
	}

	private DateTime findNextOrderDay()
	{
		/*
		 * Starting with startDate, which is today, get the next order day
		 * not including today as we are passed the cutOff time.
		 * 
		 * 	1. First get dayOfWeek from startDate
		 * 	2. Check if there is an order day greater than that and use the first one you find.
		 *  3. If can't find one greater grab the first order day in the list(it will be the closest) 
		 *     and calculate the startDate accordingly
		 *   
		 */
		if (setForOrderDayGreaterThanDayOfWeek())
			return startDate;
		else
			return setForOrderDayLessThanDayOfWeek();

	}

	private DateTime setForOrderDayLessThanDayOfWeek()
	{
		int dayOfWeek = startDate.getDayOfWeek();
		startDate = startDate.plusDays(7 - (dayOfWeek - vendorShipTime.getOrderDays()[0]));
		return startDate;

	}

	private boolean setForOrderDayGreaterThanDayOfWeek()
	{
		int dayOfWeek = startDate.getDayOfWeek();
		for (int orderDay : vendorShipTime.getOrderDays())
			if (orderDay > dayOfWeek)
			{
				startDate = startDate.plusDays(orderDay - dayOfWeek);
				return true;
			}
		return false;
	}

	public static class Builder
	{
		private VendorShipTimeDataBean vendorShipTime;
		private DateTime startDate;

		public Builder vendorShipTime( VendorShipTimeDataBean vendorShipTime )
		{
			this.vendorShipTime = vendorShipTime;
			return this;
		}

		public Builder startDate( DateTime startDate )
		{
			this.startDate = startDate;
			return this;
		}

		public WeeklyOrderStartDateCalculator build()
		{
			return new WeeklyOrderStartDateCalculator(this);
		}
	}

	private WeeklyOrderStartDateCalculator(Builder builder)
	{
		this.vendorShipTime = builder.vendorShipTime;
		this.startDate = builder.startDate;
	}
}
