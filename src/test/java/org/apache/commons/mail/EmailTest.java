//Sebastian Kuka
//CIS 376
//Assignment 3 - Test Cases and Code Coverage

package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.Map;

import javax.mail.Session;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {

    // Array of test email addresses
    String[] TEST_EMAILS = {
        "ab@bc.com",
        "cd@de.com", // Corrected to a valid email address
        "abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd"
    };

    private Email email;

    // Setup method to initialize the email object before each test
    @Before
    public void setUpEmailTest() throws Exception {
        email = new EmailConcrete();
    }

    // Setup method to initialize the MimeMessage and MailSession before each test
    @Before
    public void setUpMimeMessageAndMailSession() throws EmailException {
        // Use HtmlEmail which is a concrete subclass of Email for testing MimeMessage and MailSession methods
        email = new HtmlEmail();
        email.setHostName("hostName.example.com");
        email.setSmtpPort(25);
        email.setFrom("setFrom@example.com");
        email.addTo("addTo@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");
    }

    // Teardown method to clean up resources after each test
    @After
    public void tearDownEmailTest() throws Exception {
        email = null;
    }

    // Test case for adding Bcc recipients
    @Test
    public void testAddBcc() throws EmailException {
        email.addBcc(TEST_EMAILS);
        assertEquals("Should have three BCC addresses", 3, email.getBccAddresses().size());
    }

    // Test case for adding Cc recipients
    @Test
    public void testAddCc() throws EmailException {
        email.addCc("test@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }

    // Test case for adding headers to the email
    @Test
    public void testAddHeader() throws NoSuchFieldException, IllegalAccessException {
        String headerName = "HeaderName";
        String headerValue = "HeaderValue";

        email.addHeader(headerName, headerValue);

        Field field = Email.class.getDeclaredField("headers");
        field.setAccessible(true);
        Map<String, String> headers = (Map<String, String>) field.get(email);

        assertEquals("Header value should match", headerValue, headers.get(headerName));
    }

    // Test case for adding a null name header, expecting an IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeaderNullName() {
        email.addHeader(null, "HeaderName");
    }

    // Test case for adding a null value header, expecting an IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeaderNullValue() {
        email.addHeader("HeaderValue", null);
    }

    // Test case for adding a reply-to address
    @Test
    public void testAddReplyTo() throws EmailException {
        String replyToEmail = "noreply@example.com";
        String replyToName = "NoReply";
        Email returnedEmail = email.addReplyTo(replyToEmail, replyToName);

        // Check if the method returns 'this' for chaining method calls (fluent interface style)
        assertSame("Method should return the same Email object for chaining", email, returnedEmail);
    }

    // Test case for building a MimeMessage
    @Test
    public void testBuildMimeMessage() throws EmailException {
        email.buildMimeMessage();
        assertNotNull("MimeMessage should not be null after building", email.getMimeMessage());
    }

    // Test case for building a MimeMessage without a 'From' address, expecting EmailException
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageNoFromAddressThrowsException() throws EmailException {
        email = new HtmlEmail();
        email.setHostName("hostName.example.com");
        email.setSmtpPort(25);
        email.addTo("addTo@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");
        
        // No 'From' address set should cause EmailException
        email.buildMimeMessage();
    }
    
    // Test case for building a MimeMessage without any recipients, expecting EmailException
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageNoRecipientsThrowsException() throws EmailException {
        email = new HtmlEmail();
        email.setHostName("hostName.example.com");
        email.setSmtpPort(25);
        email.setFrom("setFrom@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // No recipients have been added should cause EmailException
        email.buildMimeMessage();
    }
    
    // Test case for building a MimeMessage without a 'From' address
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageWithoutFromAddress() throws EmailException {
        Email email = new HtmlEmail();
        email.addTo("addTo@example.com");
        email.setSubject("Test Subject");
        email.buildMimeMessage();
    }
    
 // Test case for building a MimeMessage with plain text content
    @Test
    public void testBuildMimeMessageWithPlainTextContent() throws EmailException {
        email.setSubject("Test Subject");
        email.setContent("Test Content", EmailConstants.TEXT_PLAIN);
        email.setCharset("UTF-8");

        email.buildMimeMessage();

        // Assertions to check if MimeMessage was built would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }

    // Test case for building a MimeMessage with non-plain text content
    @Test
    public void testBuildMimeMessageWithNonPlainTextContent() throws EmailException {
        email.setSubject("Test Subject");
        email.setContent("<html><body><h1>Test Content</h1></body></html>", "text/html");
        
        email.buildMimeMessage();

        // Assertions to check if MimeMessage was built with HTML content would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }

    // Test case for building a MimeMessage with just Cc recipients
    @Test
    public void testBuildMimeMessageWithJustCcRecipients() throws EmailException {
        email.addCc("cc@example.com");
        email.buildMimeMessage();

        // Assertions to check if Cc recipients were added would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }
    
    // Test case for building a MimeMessage with just Bcc recipients
    @Test
    public void testBuildMimeMessageWithJustBccRecipients() throws EmailException {
        email.addBcc("bcc@example.com");

        email.buildMimeMessage();

        // Assertions to check if Bcc recipients were added would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }

    // Test case for building a MimeMessage with just reply to addresses
    @Test
    public void testBuildMimeMessageWithJustReplyToAddresses() throws EmailException {
        email.addReplyTo("replyto@example.com", "Reply Name");

        email.buildMimeMessage();

        // Assertions to check if reply-to addresses were added would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }

    // Test case for building a MimeMessage with just headers
    @Test
    public void testBuildMimeMessageWithJustHeaders() throws EmailException {
        email.addHeader("Sebastian", "Kuka");

        email.buildMimeMessage();

        // Assertions to check if headers were added would go here
        // ... Mimic verifications by mocking if the actual mail sending is not possible
    }

    // Test case for retrieving the host name
    @Test
    public void testGetHostName() {
        Email email = new SimpleEmail();
        email.setHostName("smtp.example.com");
        assertEquals("smtp.example.com", email.getHostName());
    }

    // Test case for retrieving the host name when it's set to null
    @Test
    public void testGetHostNameWithNull() {
        Email email = new SimpleEmail();
        email.setHostName(null);
        assertNull(email.getHostName());
    }

    // Test case for retrieving the mail session
    @Test
    public void getMailSession() throws EmailException {
        Email email = new HtmlEmail();
        email.setHostName("smtp.example.com");
        email.setAuthentication("user", "password");
        Session session = email.getMailSession();
        assertNotNull("Session should not be null", session);
        assertEquals("smtp.example.com", session.getProperty("mail.smtp.host"));
    }
    
 // Test case for retrieving the mail session with an explicit host name
    @Test
    public void getMailSessionWithExplicitHostName() throws EmailException {
        Email email = new HtmlEmail();
        email.setHostName("custom.smtp.server");
        Session session = email.getMailSession();
        String host = session.getProperty("mail.smtp.host");
        assertEquals("Session hostname should match what was set", "custom.smtp.server", host);
    }

    // Test case for retrieving the sent date
    @Test
    public void testGetSentDate() {
        // Set sentDate to a known value
        Date expectedSentDate = new Date(1234567890L);
        email.setSentDate(expectedSentDate);

        Date sentDate = email.getSentDate();

        assertNotNull("Sent date should not be null", sentDate);
        assertEquals("Sent date should be same as set value", expectedSentDate.getTime(), sentDate.getTime());
    }

    // Test case for retrieving the sent date when it's null
    @Test
    public void testGetSentDateWhenNull() {
        // Assume sentDate has not been set and is null
        Date sentDate = email.getSentDate();

        assertNotNull("Sent date should not be null", sentDate);
        // Check the date is very close to 'now'
        assertTrue("Sent date should be roughly the current time",
                   Math.abs(sentDate.getTime() - System.currentTimeMillis()) < 1000);
    }

    // Test case for retrieving the socket connection timeout
    @Test
    public void testGetSocketConnectionTimeout() {
        // Assuming there is a default value set in the EmailConcrete implementation
        int timeout = email.getSocketConnectionTimeout();

        // Replace 'expectedDefaultValue' with what you expect the default to be. 
        // If you don't have a default, this could simply be 0.
        int expectedValue = 60000; // this should match the default value, if one exists
        assertEquals(expectedValue, timeout);
    }

    // Test case for setting the 'From' address
    @Test
    public void testSetFrom() throws EmailException {
        Email email = new SimpleEmail();
        email.setFrom("noreply@example.com");
        assertEquals("noreply@example.com", email.getFromAddress().getAddress());
    }
	    
	   
	
}
