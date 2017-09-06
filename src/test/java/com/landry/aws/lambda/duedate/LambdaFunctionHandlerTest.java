package com.landry.aws.lambda.duedate;

import java.io.IOException;
import org.joda.time.LocalTime;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.landry.aws.lambda.common.model.DueDateInput;
import com.landry.aws.lambda.common.model.DueDateOutput;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBean;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBeanBuilder;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

    private static DueDateInput input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = null;
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        Context ctx = createContext();

        DueDateOutput output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
    
    @Test
    public void testWeeklyOrderStartDate() {
    	Integer[] orderDays = {1,5};
    	LocalTime cutOffTime = new LocalTime(11,0);
		VendorShipTimeDataBean vendorShipTime = new VendorShipTimeDataBean.Builder().
    			orderDays(orderDays).cutOffTime(cutOffTime).build();
		WeeklyOrderStartDateCalculator wosdc = new WeeklyOrderStartDateCalculator.Builder()
				.vendorShipTime(vendorShipTime).startDate(DateTime.parse("2017-09-01")).build();
		System.out.println(wosdc.getStartDate());
    }
}
