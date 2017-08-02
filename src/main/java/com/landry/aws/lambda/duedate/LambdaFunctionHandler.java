package com.landry.aws.lambda.duedate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.landry.aws.lambda.common.model.DueDateInput;
import com.landry.aws.lambda.common.model.DueDateOutput;
import com.landry.aws.lambda.common.util.LambdaFunctions;

public class LambdaFunctionHandler implements RequestHandler<DueDateInput, DueDateOutput> {

	private static String containerId;
    @Override
    public DueDateOutput handleRequest(DueDateInput input, Context lambdaContext) {

        lambdaContext.getLogger().log("Function: " + LambdaFunctions.DUE_DATE);
		if ( input != null && input.getPing() != null && containerId!=null )
			return null;

    	if (containerId == null )
    		containerId = lambdaContext.getAwsRequestId();

		lambdaContext.getLogger().log("ContainerId: " + containerId);
		lambdaContext.getLogger().log("Input: " + input.toString());

	    DueDateServiceNoCache dds =  DueDateServiceNoCache.instance();
	    DueDateOutput ddo;
		try
		{
			ddo = dds.getMeTheBestArrivalDate(input);
		}
		catch (NullPointerException npe) {
			npe.printStackTrace();
			ddo = new DueDateOutput.Builder()
					.info("ERROR: Null pointer Exception at server. Please inform developers.")
					.build();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			e.printStackTrace();
			ddo = new DueDateOutput.Builder()
					.info(e.getMessage() + "ERROR: If not enough info please see logs amazon.")
					.build();
		}

		return  ddo;
    }
}