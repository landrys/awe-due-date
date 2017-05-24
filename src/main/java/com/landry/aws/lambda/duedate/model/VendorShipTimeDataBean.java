package com.landry.aws.lambda.duedate.model;

import java.math.BigInteger;
import java.util.Arrays;

import org.joda.time.LocalTime;

public class VendorShipTimeDataBean
{
	private Long id;
	private BigInteger vendorId;
	private String vendorName; // This will need to be set from DB call to vendor.
	private String warehouse;
	private String shippingCarrier;
	private int businessDays;
	private int shippingDays;
	private boolean bike;
	private boolean weeklyOrder;
	private Integer[] orderDays;
	private LocalTime cutOffTime;
	private boolean dropShipToStore;

	public BigInteger getVendorId()
	{
		return vendorId;
	}

	public void setVendorId( BigInteger vendorId )
	{
		this.vendorId = vendorId;
	}

	public String getWarehouse()
	{
		return warehouse;
	}

	public void setWarehouse( String warehouse )
	{
		this.warehouse = warehouse;
	}

	public String getShippingCarrier()
	{
		return shippingCarrier;
	}

	public void setShippingCarrier( String shippingCarrier )
	{
		this.shippingCarrier = shippingCarrier;
	}

	public int getShippingDays()
	{
		return shippingDays;
	}

	public void setShippingDays( int shippingDays )
	{
		this.shippingDays = shippingDays;
	}

	public boolean isBike()
	{
		return bike;
	}

	public void setBike( boolean bike )
	{
		this.bike = bike;
	}

	public boolean isWeeklyOrder()
	{
		return weeklyOrder;
	}

	public void setWeeklyOrder( boolean weeklyOrder )
	{
		this.weeklyOrder = weeklyOrder;
	}

	public Integer[] getOrderDays()
	{
		return orderDays;
	}

	public void setOrderDays( Integer[] orderDays )
	{
		this.orderDays = orderDays;
	}

	public LocalTime getCutOffTime()
	{
		return cutOffTime;
	}

	public void setCutOffTime( LocalTime cutOffTime )
	{
		this.cutOffTime = cutOffTime;
	}

	public boolean isDropShipToStore()
	{
		return dropShipToStore;
	}

	public void setDropShipToStore( boolean dropShipToStore )
	{
		this.dropShipToStore = dropShipToStore;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName( String vendorName )
	{
		this.vendorName = vendorName;
	}

	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return "VendorShipTime [id=" + id + ", vendorId=" + vendorId + ", vendorName=" + vendorName + ", warehouse="
				+ warehouse + ", shippingCarrier=" + shippingCarrier + ", businessDays=" + businessDays
				+ ", shippingDays=" + shippingDays + ", bike=" + bike + ", weeklyOrder=" + weeklyOrder + ", orderDays="
				+ Arrays.toString(orderDays) + ", cutOffTime=" + cutOffTime + ", dropShipToStore=" + dropShipToStore
				+ "]";
	}

	public static class Builder
	{
		private Long id;
		private BigInteger vendorId;
		private String vendorName;
		private String warehouse;
		private String shippingCarrier;
		private int businessDays;
		private int shippingDays;
		private boolean bike;
		private boolean weeklyOrder;
		private Integer[] orderDays;
		private LocalTime cutOffTime;
		private boolean dropShipToStore;

		public Builder id( Long id )
		{
			this.id = id;
			return this;
		}

		public Builder vendorId( BigInteger vendorId )
		{
			this.vendorId = vendorId;
			return this;
		}

		public Builder vendorName( String vendorName )
		{
			this.vendorName = vendorName;
			return this;
		}

		public Builder warehouse( String warehouse )
		{
			this.warehouse = warehouse;
			return this;
		}

		public Builder shippingCarrier( String shippingCarrier )
		{
			this.shippingCarrier = shippingCarrier;
			return this;
		}

		public Builder businessDays( int businessDays )
		{
			this.businessDays = businessDays;
			return this;
		}

		public Builder shippingDays( int shippingDays )
		{
			this.shippingDays = shippingDays;
			return this;
		}

		public Builder bike( boolean bike )
		{
			this.bike = bike;
			return this;
		}

		public Builder weeklyOrder( boolean weeklyOrder )
		{
			this.weeklyOrder = weeklyOrder;
			return this;
		}

		public Builder orderDays( Integer[] orderDays )
		{
			this.orderDays = orderDays;
			return this;
		}

		public Builder cutOffTime( LocalTime cutOffTime )
		{
			this.cutOffTime = cutOffTime;
			return this;
		}

		public Builder dropShipToStore( boolean dropShipToStore )
		{
			this.dropShipToStore = dropShipToStore;
			return this;
		}

		public VendorShipTimeDataBean build()
		{
			return new VendorShipTimeDataBean(this);
		}
	}

	private VendorShipTimeDataBean(Builder builder)
	{
		this.id = builder.id;
		this.vendorId = builder.vendorId;
		this.vendorName = builder.vendorName;
		this.warehouse = builder.warehouse;
		this.shippingCarrier = builder.shippingCarrier;
		this.businessDays = builder.businessDays;
		this.shippingDays = builder.shippingDays;
		this.bike = builder.bike;
		this.weeklyOrder = builder.weeklyOrder;
		this.orderDays = builder.orderDays;
		this.cutOffTime = builder.cutOffTime;
		this.dropShipToStore = builder.dropShipToStore;
	}

	public int getBusinessDays()
	{
		return businessDays;
	}

	public void setBusinessDays( int businessDays )
	{
		this.businessDays = businessDays;
	}
}
