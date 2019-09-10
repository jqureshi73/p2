package com.ilinksolutions.p2.rservices;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ilinksolutions.p2.bservices.UKVisaService;
import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.exceptions.EntityNotFoundException;
import com.ilinksolutions.p2.exceptions.ErrorCode;
import com.ilinksolutions.p2.exceptions.RequiredFieldMissingException;
import com.ilinksolutions.p2.exceptions.USCISException;
import com.ilinksolutions.p2.exceptions.UnProcessableEntityException;

@RestController
public class P2RestController
{
	Logger logger = LoggerFactory.getLogger(P2RestController.class);
	
	@GetMapping("/serviceCheck")
	public String serviceCheck() {
		return "P2 Service is Running...";
	}
	
    @GetMapping("/getmsg/{id}")
    public ResponseEntity<UKVisaMessage> readEntry(@PathVariable String id) 
    {
    	logger.info("P2RestController: readEntry: Begin.");
    	logger.info("P2RestController: readEntry: Path Variable: " + id);
    	if (isStringInt(id)) {
	    	try {
		        UKVisaService service = new UKVisaService();
		        UKVisaMessage returnValue = service.getEntry(new Integer(id).intValue());
		        if (returnValue == null || returnValue.getId() == 0)
		        {
		        	logger.info("P2RestController: readEntry: returnValue: NULL");
		            throw new EntityNotFoundException(Integer.valueOf(id));
		        }
		        else
		        {
		            logger.info("P2RestController: readEntry: returnValue: " + returnValue.toString());
		            return ResponseEntity.ok(returnValue);
		        }
	    	} catch (Exception e) {
	    		logger.error("P2RestController: readEntry: " + e);
	            throw new EntityNotFoundException(Integer.valueOf(id));
	    	}
      } else {
    	  throw new USCISException("ID should be in number format!", ErrorCode.BAD_REQUEST_ERROR_CODE);
      }
    }
    
    @PostMapping("/savemsg")
    public ResponseEntity<UKVisaMessage> registerMessage(@RequestBody UKVisaMessage message)
    {
    	logger.info("registerMessage: registerMessage: Begin.");
    	logger.info("registerMessage: registerMessage: Transform: " + message.toString());
    	if (message != null && (message.getId() == 0 || StringUtils.isBlank(message.getFirstName()) || StringUtils.isBlank(message.getLastName()))) {
    		getRequiredFields(message);
    	} else if (message != null && StringUtils.isNotBlank(message.getEmail()) && !isEmailValid(message.getEmail())){
    			throw new USCISException(message.getEmail()+ " is not a valid Email.", ErrorCode.BAD_REQUEST_ERROR_CODE);
    		
    	}
    	try {
	    	UKVisaService service = new UKVisaService();
	    	UKVisaMessage returnValue = service.addEntry(message);
	    	if (returnValue == null)
	    	{
	    		logger.info("registerMessage: registerMessage: id: NULL.");
	           throw new UnProcessableEntityException(message.getFirstName());
	        }
	    	else
	    	{
	    		logger.info("registerMessage: registerMessage: id: End.");
	            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(returnValue.getId()).toUri();
	            return ResponseEntity.created(uri).body(returnValue);
	        }
    	} catch (Exception e) {
    		logger.error("P2RestController: registerMessage: " + e);
            throw new UnProcessableEntityException(message.getFirstName());
    	}
    }
    
    @PutMapping("/updatemsg/{id}")
    public ResponseEntity<UKVisaMessage> update(@RequestBody UKVisaMessage message, @PathVariable int id)
    {
    	String msg = (StringUtils.isBlank(message.getFirstName()) ? "firstName" : "");
		msg += (msg.length() >0 && StringUtils.isBlank(message.getLastName())? ", " : "") + (StringUtils.isBlank(message.getLastName()) ? "lastName" : "");
		
		if (msg.length() > 0) {
			logger.error("Following Required Fields are Missing: " + msg);
			throw new RequiredFieldMissingException(msg);
		} else if (message != null && StringUtils.isNotBlank(message.getEmail()) && !isEmailValid(message.getEmail())){
			throw new USCISException(message.getEmail()+ " is not a valid Email.", ErrorCode.BAD_REQUEST_ERROR_CODE);
		}
		
		try {
			UKVisaService service = new UKVisaService();
			UKVisaMessage returnValue = service.updateEntry(id, message);
	        if (returnValue == null)
	        {
	        	logger.error("P2RestController: Failed to update the data for id: "+id );
	        	throw new UnProcessableEntityException(Integer.valueOf(id));
	        }
	        else
	        {
	            return ResponseEntity.ok(returnValue);
	        }
        
		} catch (Exception e) {
    		logger.error("P2RestController: update: " + e);
            throw new UnProcessableEntityException(Integer.valueOf(id));
    	}
    }
    

	private void getRequiredFields(UKVisaMessage message) {
		String msg = message.getId() == 0 ? " id" : "";
		msg += (msg.length() >0 ? ", " : "") + (StringUtils.isBlank(message.getFirstName()) ? " firstName" : "");
		msg += ((msg.length() >0 && StringUtils.isBlank(message.getLastName())) ? ", " : "") + (StringUtils.isBlank(message.getLastName()) ? " lastName" : "");
		logger.error("Following Required Fields are Missing: " + msg);
		throw new USCISException("Missing required field(s): "+ msg, ErrorCode.BAD_REQUEST_ERROR_CODE);
	}
	
	private boolean isStringInt(String s)
	{
	    try
	    {
	        Integer.parseInt(s);
	        return true;
	    } catch (Exception ex)
	    {
	        return false;
	    }
	}
	
    private boolean isEmailValid(String email) {
	   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	   return email.matches(regex);
	}
}