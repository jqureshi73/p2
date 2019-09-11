package com.ilinksolutions.p2.bservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.data.UKVisaDAO;
import com.ilinksolutions.p2.data.impl.UKVisaDAOImpl;
import com.ilinksolutions.p2.domains.UKVisaMessage;

/**
 *
 */
public class UKVisaService
{
	private UKVisaDAO dao = new UKVisaDAOImpl();
	Logger logger = LoggerFactory.getLogger(UKVisaService.class);

	public UKVisaMessage getEntry(int id)
	{
		return dao.getEntry(id);
	}
	
	public List<UKVisaMessage> getAllEntries()
	{
		return dao.list();
	}
	
	public UKVisaMessage addEntry(UKVisaMessage message)
	{
		UKVisaMessage returnValue = dao.save(message);
		returnValue = sendEmailClient(returnValue);
		return returnValue;
	}

	public UKVisaMessage updateEntry(int id, UKVisaMessage message)
	{
		sendEmailClient(message);
		return dao.updateEntry(id, message);
	}
	
	public UKVisaMessage sendEmailClient(UKVisaMessage message)
    {
        String text = "Dear " + message.getFirstName() + " " + message.getLastName() + 
                ", \n\n Your application has been submitted based on your a request filed on your behalf.";
        String subject = "Re: UK VISA Application: Submission Added.";
        try
        {
            //UKVisaMessage returnValue = dao.save(message);
            String messageString = "{\"id\": " + message.getId() + "," +
                    "\"firstName\": \"" + message.getFirstName() + "\"," +
                    "\"lastName\": \"" + message.getLastName() + "\"," +
                    "\"contactNo\": \"" + message.getContactNo() + "\"," +
                    "\"email\": \"" + message.getEmail() + "\"}";
            URL url = new URL("http://ilinkp2v17-ilinkp2v17.b9ad.pro-us-east-1.openshiftapps.com/p3m1/sendEmail");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream oStream = httpConnection.getOutputStream();
            oStream.write(messageString.getBytes());
            oStream.flush();
            if (httpConnection.getResponseCode() != 200)
            {
                throw new RuntimeException("Unsuccessful: Error Code: " + httpConnection.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
            String output;
            while ((output = br.readLine()) != null)
            {
                logger.info("Output from REST service API: " + output);
            }
            httpConnection.disconnect();
            //return returnValue;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
		return message;
    }
}
