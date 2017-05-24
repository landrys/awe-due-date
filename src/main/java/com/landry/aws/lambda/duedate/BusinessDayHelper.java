package com.landry.aws.lambda.duedate;

import org.joda.time.DateTime;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.landry.aws.lambda.common.invoker.DateBusinessDaysOut;
import com.landry.aws.lambda.common.model.BusinessDayInput;
import com.landry.aws.lambda.common.model.BusinessDayOutput;
import com.landry.aws.lambda.common.util.MyDateUtil;


public class BusinessDayHelper
{
	private  static BusinessDayHelper instance;
	private static DateBusinessDaysOut dateBusinessDaysOutService;

	private BusinessDayHelper() {
	    dateBusinessDaysOutService = LambdaInvokerFactory.builder()
			.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(DateBusinessDaysOut.class);
    }

	public static BusinessDayHelper instance() {

        if (instance == null) {
            synchronized(BusinessDayHelper.class) {
                if (instance == null)
                    instance = new BusinessDayHelper();
            }
        }
        return instance;
    }

	public DateTime moveForward( int businessDays, DateTime startDate )
	{
		BusinessDayInput input = new BusinessDayInput();
		input.setBusinessDays(businessDays);
		input.setDate(MyDateUtil.toStringDate(startDate, "MM-dd-yyyy"));
		BusinessDayOutput out = dateBusinessDaysOutService.dateBusinessDaysOut(input);
		DateTime arrivalDate = MyDateUtil.toDateTime(out.getDate(), "MM-dd-yyyy");
		return arrivalDate;
	}

}
