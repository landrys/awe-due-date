package com.landry.aws.lambda.duedate;

import org.joda.time.DateTime;

import com.landry.aws.lambda.businessday.BusinessDayService;
import com.landry.aws.lambda.common.util.MyDateUtil;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBean;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBeanBuilder2;

public class DueDateCalculatorNoCache
{
	private DateTime arrivalDate;
	private DateTime startDate;
	private Integer vendorShipTimeId;
	private VendorShipTimeDataBean vendorShipTime;
	private String store;

	public DateTime getArrivalDate2() throws Exception
	{
		loadVendorShipTime();
		calculateArrivalDate();
		return arrivalDate;
	}

	private void loadVendorShipTime() throws Exception
	{
		VendorShipTimeDataBeanBuilder2 vstb2 = new VendorShipTimeDataBeanBuilder2(vendorShipTimeId);
		vendorShipTime = vstb2.getVendorShipTime();
		// DEBUG
		System.out.println(vendorShipTime);
	}

	private void calculateArrivalDate()
	{
		if (vendorShipTime.isWeeklyOrder()) {
			startDate = getStartDateForWeeklyOrder();
		} else {
			// This can be a weekend day or holiday. The ArrivalDate stuff
			// will take care of moving it to a business day.
			startDate = getStartDateForRegularOrder();
		}

		arrivalDate = BusinessDayService.moveForward(vendorShipTime.getShippingDays(), startDate);


		if (!store.equalsIgnoreCase("Natick") && !vendorShipTime.isDropShipToStore())
			arrivalDate = BusinessDayService.moveForward(1, arrivalDate);

		if (vendorShipTime.isBike())
			arrivalDate = BusinessDayService.moveForward(1, arrivalDate);
	}

	private DateTime getStartDateForWeeklyOrder()
	{
		WeeklyOrderStartDateCalculator wosdc = new WeeklyOrderStartDateCalculator.Builder()
				.vendorShipTime(vendorShipTime).startDate(DateTime.now()).build();
		return wosdc.getStartDate();
	}

	private DateTime getStartDateForRegularOrder()
	{
		return BusinessDayService.moveForward(vendorShipTime.getBusinessDays(),
				MyDateUtil.addDayToNowIfPastCutOffTime(vendorShipTime.getCutOffTime()));
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public static class Builder
	{
		private Integer vendorShipTimeId;
		private String store;

		public Builder vendorShipTimeId( Integer vendorShipTimeId )
		{
			this.vendorShipTimeId = vendorShipTimeId;
			return this;
		}

		public Builder store( String store )
		{
			this.store = store;
			return this;
		}

		public DueDateCalculatorNoCache build()
		{
			return new DueDateCalculatorNoCache(this);
		}
	}

	private DueDateCalculatorNoCache(Builder builder)
	{
		this.vendorShipTimeId = builder.vendorShipTimeId;
		this.store = builder.store;
	}
}
