package com.landry.aws.lambda.duedate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.landry.aws.lambda.common.model.DueDateInput;
import com.landry.aws.lambda.common.model.DueDateOutput;

public class LambdaFunctionHandler implements RequestHandler<DueDateInput, DueDateOutput> {

    @Override
    public DueDateOutput handleRequest(DueDateInput input, Context lambdaContext) {

		lambdaContext.getLogger().log("Input: " + input.toString());

	    DueDateService dds =  DueDateService.instance();
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
		/*
	    DueDateCalculator ddc = context.getBean(DueDateCalculator.class, input.getVendorShipTimeIds().get(0), input.getStore() );
		DueDateOutput ddo = null;

        try
		{
			DateTime arrivalDate =  ddc.getArrivalDate2();
			ddo = new DueDateOutput.Builder()
					.arrivalDate(arrivalDate.toString())
					//.vendorShipTimeId(ddc.todo)
					.info("Success")
					.build();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			ddo = new DueDateOutput.Builder()
					.info(e.getMessage() + "If not enough info please see logs amazon.")
					.build();
		}
		*/

		return  ddo;
    }
}
/* 
 *     private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}


		AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
    	S3Object xFile = client.getObject("lambda-function-bucket-us-east-1-1493065008443", ".credentials/landryeleven/StoredCredential");
    	InputStream contents = xFile.getObjectContent();

    	context.getLogger().log(getStringFromInputStream(contents));
    	context.getLogger().log("The user.home property:" + System.getProperty("user.home"));
 
*/