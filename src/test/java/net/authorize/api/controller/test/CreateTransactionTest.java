package net.authorize.api.controller.test;

import junit.framework.Assert;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.CustomerProfilePaymentType;
import net.authorize.api.contract.v1.CustomerProfileType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessagesType;
import net.authorize.api.contract.v1.NameAndAddressType;
import net.authorize.api.contract.v1.PaymentProfile;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.controller.CreateTransactionController;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateTransactionTest extends ApiCoreTestBase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApiCoreTestBase.setUpBeforeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ApiCoreTestBase.tearDownAfterClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testAuthCaptureTogetherCreateTransactionRequest() {
		
		TransactionRequestType transactionRequestType = new TransactionRequestType();
		transactionRequestType.setTransactionType( TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		transactionRequestType.setAmount( setValidTransactionAmount(counter));
		transactionRequestType.setPayment( paymentOne);
		//transactionRequestType.setAuthCode( authCode);
		//transactionRequestType.setRefTransId( refId);
		//transactionRequestType.setSplitTenderId( splitTenderId);
		transactionRequestType.setOrder( orderType);
		//transactionRequestType.setLineItems( lineItems);
		//transactionRequestType.setTax( setValidTaxAmount(transactionRequestType.getAmount()));
		//transactionRequestType.setDuty( duty);
		//transactionRequestType.setShipping( shipping);
		//transactionRequestType.setTaxExempt( taxExempt);
		//transactionRequestType.setPoNumber( poNumber);
		transactionRequestType.setCustomer( customerDataOne);
		transactionRequestType.setBillTo( customerAddressOne);
		//transactionRequestType.setShipTo( customerAddressOne);
		//transactionRequestType.setCustomerIP( customerIP);
		//transactionRequestType.setCardholderAuthentication( cardholderAuthentication);
		//transactionRequestType.setRetail( retail);
		//transactionRequestType.setCustomerProfile( customerProfile);
		//transactionRequestType.setCurrencyCode( currencyCode);
		//transactionRequestType.setSolutionId( solutionId);
		//transactionRequestType.setTransactionSettings( transactionSettings);
		//transactionRequestType.setUserFields( userFields);
 
		CreateTransactionRequest createRequest = new CreateTransactionRequest();
		createRequest.setMerchantAuthentication(cnpMerchantAuthenticationType);
		createRequest.setRefId(refId);
		createRequest.setTransactionRequest(transactionRequestType);

		CreateTransactionResponse createResponse = executeTestRequestWithSuccess(createRequest, CreateTransactionController.class, environment);
		processTransactionResponse(createResponse);
		
	}

	//@Test //TODO
	public void testAuthCaptureSeparateCreateTransactionRequest() {
		
		TransactionRequestType transactionRequestType = new TransactionRequestType();
		transactionRequestType.setTransactionType( TransactionTypeEnum.AUTH_ONLY_CONTINUE_TRANSACTION.value());
		transactionRequestType.setAmount( setValidTransactionAmount(counter));
		transactionRequestType.setPayment( paymentOne);
		//transactionRequestType.setAuthCode( authCode);
		//transactionRequestType.setRefTransId( refId);
		//transactionRequestType.setSplitTenderId( splitTenderId);
		transactionRequestType.setOrder( orderType);
		//transactionRequestType.setLineItems( lineItems);
		//transactionRequestType.setTax( setValidTaxAmount(transactionRequestType.getAmount()));
		//transactionRequestType.setDuty( duty);
		//transactionRequestType.setShipping( shipping);
		//transactionRequestType.setTaxExempt( taxExempt);
		//transactionRequestType.setPoNumber( poNumber);
		transactionRequestType.setCustomer( customerDataOne);
		transactionRequestType.setBillTo( customerAddressOne);
		//transactionRequestType.setShipTo( customerAddressOne);
		//transactionRequestType.setCustomerIP( customerIP);
		//transactionRequestType.setCardholderAuthentication( cardholderAuthentication);
		//transactionRequestType.setRetail( retail);
		//transactionRequestType.setCustomerProfile( customerProfile);
		//transactionRequestType.setCurrencyCode( currencyCode);
		//transactionRequestType.setSolutionId( solutionId);
		//transactionRequestType.setTransactionSettings( transactionSettings);
		//transactionRequestType.setUserFields( userFields);
 
		CreateTransactionRequest createRequest = new CreateTransactionRequest();
		createRequest.setMerchantAuthentication(cnpMerchantAuthenticationType);
		createRequest.setRefId(refId);
		createRequest.setTransactionRequest(transactionRequestType);

		CreateTransactionResponse createResponse = executeTestRequestWithSuccess(createRequest, CreateTransactionController.class, environment);
		processTransactionResponse(createResponse);
	}

	@Test
	public void validateCreateTransactionEmptyPaymentProfile() {
		
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, 0);
		holder.customerProfilePaymentType.getPaymentProfile().setPaymentProfileId(null);
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		//add customer profile with payment profile with empty profileId and payment data
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( paymentOne);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		final String errorMessage = "The element 'paymentProfile' in namespace 'AnetApi/xml/v1/schema/AnetApiSchema.xsd' has incomplete content. List of possible elements expected: 'paymentProfileId' in namespace 'AnetApi/xml/v1/schema/AnetApiSchema.xsd'.";
		MessagesType errorMessageType = (null == failureResponse) ? getErrorResponse().getMessages() : failureResponse.getMessages();
		//the missing profileId element in profile element results in XSD validation failure
		//Message: 'E00003' instead of "E00090"
		validateErrorCode(errorMessageType,"E00003");
		Assert.assertEquals( errorMessage, getFirstErrorText(errorMessageType));
	}
	
	@Test
	public void validateCreateTransactionPaymentAndPaymentProfileWithId() {

		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, 0);
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		//add customer profile with payment profile and payment data 
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( paymentOne);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00091");
	}
	
	@Test
	public void validateCreateTransactionMissingPaymentWithShippingProfileId() {
		//existing behavior
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, counter);
		
		CreateTransactionRequest createRequest = holder.createRequest;
		CustomerProfilePaymentType customerProfilePaymentType = holder.customerProfilePaymentType;
		customerProfilePaymentType.setPaymentProfile(null);
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		//add shipping profile with no payment ship to address
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( customerAddressOne);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00027");
	}

	@Test
	public void validateCreateTransactionShipToAndShippingProfileIdWithPayment() {
		
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, counter);

		CreateTransactionRequest createRequest = holder.createRequest;
		CustomerProfilePaymentType customerProfilePaymentType = holder.customerProfilePaymentType;
		customerProfilePaymentType.setPaymentProfile(null);
		TransactionRequestType transactionRequestType = holder.transactionRequestType;
		
		//add shipping profile with ship to address with payment
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( paymentOne);
		transactionRequestType.setShipTo( customerAddressOne);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00092");
	}

	@Test
	public void validateCreateTransactionShipToAndShippingProfileIdWithPaymentProfileId() {
		
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, counter);
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		//add shipping profile with ship to address with payment profile
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( customerAddressOne);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00092");
	}
	
	@Test
	public void validateCreateTransactionPaymentProfileWithBillTo() {

		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, counter, 0);
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		//add shipping profile with no payment ship to address
		transactionRequestType.setBillTo(customerAddressOne);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo(null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00093");
	}
	
	@Test
	public void validateCreateTransactionMissingPaymentAndShippingProfileWithCustomerProfile() {
		
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(counter, 0, 0);
		holder.customerProfilePaymentType.setPaymentProfile(null);
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;
		
		//null payment profile and shipping profile, but has customer profile   
		transactionRequestType.setBillTo(customerAddressOne);
		transactionRequestType.setPayment( paymentOne);
		transactionRequestType.setShipTo( customerAddressOne);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00095");
	}

	@Test
	public void validateCreateTransactionNonExistingCustomerProfileId() {
		CustomerProfileElementsHolder holder = 
				setUpRequestForCreateTransaction(new CustomerProfileIdsHolder(999999999,999999999,0));
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		transactionRequestType.setBillTo( null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		validateErrorCode( failureResponse.getMessages(), "E00040");
		final String errorMessage = "Customer Profile ID or Customer Payment Profile ID not found.";		
		Assert.assertEquals( errorMessage, getFirstErrorText(failureResponse.getMessages()));
	}

	@Test
	public void validateCreateTransactionNonExistingCustomerPaymentProfileId() {
		CustomerProfileIdsHolder profileIdHolder = setupCustomersWithProfile(cnpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);

		int customerProfileId = Integer.valueOf(profileIdHolder.CustomerProfileId);
		CustomerProfileElementsHolder holder = 
				setUpRequestForCreateTransaction(new CustomerProfileIdsHolder(customerProfileId, 999999999,0));
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		transactionRequestType.setBillTo( null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		teardownCustomersWithProfile(cnpMerchantAuthenticationType, profileIdHolder, refId);

		validateErrorCode( failureResponse.getMessages(), "E00040");
		final String errorMessage = "Customer Profile ID or Customer Payment Profile ID not found.";		
		Assert.assertEquals( errorMessage, getFirstErrorText(failureResponse.getMessages()));
	}

	@Test
	public void validateCreateTransactionNonExistingShippingProfileId() {
		CustomerProfileIdsHolder profileIdHolder = setupCustomersWithProfile(cnpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);

		int customerProfileId = Integer.valueOf(profileIdHolder.CustomerProfileId);
		int paymentProfileId = Integer.valueOf(profileIdHolder.CustomerPaymentProfileId);
		CustomerProfileElementsHolder holder = 
				setUpRequestForCreateTransaction(new CustomerProfileIdsHolder(customerProfileId, paymentProfileId, 999999999));
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		transactionRequestType.setBillTo( null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		teardownCustomersWithProfile(cnpMerchantAuthenticationType, profileIdHolder, refId);

		validateErrorCode( failureResponse.getMessages(), "E00040");
		final String errorMessage = "Customer Shipping Address ID not found.";		
		Assert.assertEquals( errorMessage, getFirstErrorText(failureResponse.getMessages()));
	}

	@Test
	public void validateCreateTransactionMisMatchProfileIds() {
		CustomerProfileIdsHolder profileIdHolder1 = setupCustomersWithProfile(cnpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);
		CustomerProfileIdsHolder profileIdHolder2 = setupCustomersWithProfile(cpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);

		int customerProfileId = Integer.valueOf(profileIdHolder1.CustomerProfileId);
		int paymentProfileId = Integer.valueOf(profileIdHolder2.CustomerPaymentProfileId);
		CustomerProfileElementsHolder holder = 
				setUpRequestForCreateTransaction(new CustomerProfileIdsHolder(customerProfileId, paymentProfileId, 0));
		CreateTransactionRequest createRequest = holder.createRequest;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		transactionRequestType.setBillTo( null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		teardownCustomersWithProfile(cpMerchantAuthenticationType, profileIdHolder2, refId);
		teardownCustomersWithProfile(cnpMerchantAuthenticationType, profileIdHolder1, refId);

		validateErrorCode( failureResponse.getMessages(), "E00040");
		final String errorMessage = "Customer Profile ID or Customer Payment Profile ID not found.";		
		Assert.assertEquals( errorMessage, getFirstErrorText(failureResponse.getMessages()));
	}
	
	//SKIP @Test
	public void testProfileTestCreateTransactionRequest() {
		CustomerProfileIdsHolder profileHolder1 = setupCustomersWithProfile(cnpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);
		CustomerProfileIdsHolder profileHolder2 = setupCustomersWithProfile(cpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);
			
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction(profileHolder1);
		CreateTransactionRequest createRequest = holder.createRequest;
		CustomerProfilePaymentType customerProfilePaymentType = holder.customerProfilePaymentType;
		
//		PaymentProfile paymentProfile = holder.paymentProfile;
		TransactionRequestType transactionRequestType = holder.transactionRequestType;

		transactionRequestType.setBillTo( null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		customerProfilePaymentType.setPaymentProfile( customerProfilePaymentType.getPaymentProfile());
		customerProfilePaymentType.setShippingProfileId(null);

		CreateTransactionResponse failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		
		teardownCustomersWithProfile(cpMerchantAuthenticationType, profileHolder2, refId);
		teardownCustomersWithProfile(cnpMerchantAuthenticationType, profileHolder1, refId);
		Assert.assertEquals( "E00001", getFirstErrorCode(failureResponse.getMessages()));

		
		CreateTransactionResponse successResponse = executeTestRequestWithSuccess(createRequest, CreateTransactionController.class, environment);
		TransactionResponse transactionResponse = successResponse.getTransactionResponse();
		Assert.assertNotNull(transactionResponse);

		NameAndAddressType shipTo = transactionResponse.getShipTo();
		//GETTING NULL
		//Assert.assertNotNull(shipTo);

		//try in-valid combinations of customer profile elements
		//non-existing or belonging to other customer/ merchant
		PaymentProfile newPaymentProfile = null;
		String newShippingProfile = null;
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		customerProfilePaymentType.setPaymentProfile(newPaymentProfile);
		customerProfilePaymentType.setShippingProfileId(newShippingProfile);
	//	paymentProfile.setPaymentProfileId(counterStr);
		failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		Assert.assertEquals( "E00001", getFirstErrorCode(failureResponse.getMessages()));

/*
		transactionRequestType.setBillTo(null);
		transactionRequestType.setPayment( null);
		transactionRequestType.setShipTo( null);
		customerProfilePaymentType.setPaymentProfile(newPaymentProfile);
		customerProfilePaymentType.setShippingProfileId(newShippingProfile);
		paymentProfile.setPaymentProfileId(counterStr);
		failureResponse = executeTestRequestWithFailure(createRequest, CreateTransactionController.class, environment);
		Assert.assertEquals( "E00001", getFirstErrorCode(failureResponse.getMessages()));
*/
	}
	
	@Test
	public void testSetupCustomersWithProfile() {
		CustomerProfileIdsHolder profileHolder1 = setupCustomersWithProfile(cnpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);
		CustomerProfileIdsHolder profileHolder2 = setupCustomersWithProfile(cpMerchantAuthenticationType, customerProfileType, customerPaymentProfileOne, customerAddressOne, refId);
			
		teardownCustomersWithProfile(cpMerchantAuthenticationType, profileHolder2, refId);
		teardownCustomersWithProfile(cnpMerchantAuthenticationType, profileHolder1, refId);
	}
	
	private void processTransactionResponse( CreateTransactionResponse createResponse) {
		
		Assert.assertNotNull(createResponse.getTransactionResponse());
		TransactionResponse transactionResponse = createResponse.getTransactionResponse();
		Assert.assertNotNull(transactionResponse.getTransId());
		logger.info(String.format("Created Transaction: %s", transactionResponse.getTransId()));
		MyTransactionResponse myResponse = this.createTransactionResponse(transactionResponse);
		logger.info(String.format("Result: %s", myResponse.toString()));
	}

	private CreateTransactionTest.MyTransactionResponse createTransactionResponse(TransactionResponse response)
	{
		CreateTransactionTest.MyTransactionResponse result = this.new MyTransactionResponse();
		
		result.setResponseCode( response.getResponseCode());
		result.setRawResponseCode( response.getRawResponseCode());
		result.setAuthCode( response.getAuthCode());
		result.setAvsResultCode( response.getAvsResultCode());
		result.setCvvResultCode( response.getCvvResultCode());
		result.setCavvResultCode( response.getCavvResultCode());
		result.setTransId( response.getTransId());
		result.setRefTransID( response.getRefTransID());
		result.setTransHash( response.getTransHash());
		result.setTestRequest( response.getTestRequest());
		result.setAccountNumber( response.getAccountNumber());
		result.setAccountType( response.getAccountType());
		result.setSplitTenderId( response.getSplitTenderId());
		result.setPrePaidCard( response.getPrePaidCard());
		result.setMessages( response.getMessages());
		result.setErrors( response.getErrors());
		result.setSplitTenderPayments( response.getSplitTenderPayments());
		result.setUserFields( response.getUserFields());
		result.setShipTo( response.getShipTo());
		result.setSecureAcceptance( response.getSecureAcceptance());
		
		return result;
	}

	private CustomerProfileElementsHolder setUpRequestForCreateTransaction(int customerProfileId, int customerPaymentProfileId, int customerShippingAddressId) {
		CustomerProfileIdsHolder profileIdsHolder = new CustomerProfileIdsHolder(customerProfileId, customerPaymentProfileId, customerShippingAddressId);  		
		return setUpRequestForCreateTransaction(profileIdsHolder);
	}
	
	private CustomerProfileElementsHolder setUpRequestForCreateTransaction(CustomerProfileIdsHolder profileIdsHolder) {
		CustomerProfileElementsHolder holder = setUpRequestForCreateTransaction();

		holder.customerProfilePaymentType.setCustomerProfileId(profileIdsHolder.CustomerProfileId);
		holder.customerProfilePaymentType.setShippingProfileId(profileIdsHolder.CustomerShippingAddressId);
		if ( null != holder.customerProfilePaymentType.getPaymentProfile())
		{
			holder.customerProfilePaymentType.getPaymentProfile().setPaymentProfileId(profileIdsHolder.CustomerPaymentProfileId);
		}
		
		return holder;
	}
	
	private CustomerProfileElementsHolder setUpRequestForCreateTransaction() {
		PaymentProfile paymentProfile  = new PaymentProfile();
		return setUpRequestForCreateTransaction( paymentProfile); 
	}
	
	private CustomerProfileElementsHolder setUpRequestForCreateTransaction(PaymentProfile paymentProfile) {

		CreateTransactionRequest createRequest = new CreateTransactionRequest();
		TransactionRequestType transactionRequestType = new TransactionRequestType();
		CustomerProfilePaymentType customerProfilePaymentType = new CustomerProfilePaymentType();
		

		CustomerProfileElementsHolder customerProfileElementsHolder = new CustomerProfileElementsHolder();
		customerProfileElementsHolder.createRequest = createRequest;
		customerProfileElementsHolder.customerProfilePaymentType = customerProfilePaymentType;
		if (null != customerProfilePaymentType)
		{
			customerProfilePaymentType.setPaymentProfile(paymentProfile);
		}
		customerProfileElementsHolder.transactionRequestType = transactionRequestType;
		
		//transactionRequestType.setTransactionType( TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.toString());
		transactionRequestType.setTransactionType( TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		transactionRequestType.setAmount( setValidTransactionAmount(counter));

		//add customer profile  
		transactionRequestType.setProfile(customerProfilePaymentType);

		customerProfilePaymentType.setCustomerProfileId(counterStr);//customerProfileType.getMerchantCustomerId()); 
		//customerProfilePaymentType.setCreateProfile(true);
		
		transactionRequestType.setOrder( orderType);
		transactionRequestType.setCustomer( customerDataOne);
 
		createRequest.setMerchantAuthentication(cnpMerchantAuthenticationType);
		createRequest.setRefId(refId);
		createRequest.setTransactionRequest(transactionRequestType);
		
		
		return customerProfileElementsHolder;
	}
	
	public static CustomerProfileIdsHolder setupCustomersWithProfile(MerchantAuthenticationType merchantAuthenticationType, CustomerProfileType customerProfileType, CustomerPaymentProfileType paymentProfileType, CustomerAddressType addressType, String referenceId) {
		//create a new customer profile
		String customerProfileId = CustomerProfileTest.createCustomerProfile( merchantAuthenticationType, paymentProfileType, customerProfileType, referenceId);
		String customerPaymentProfileId = CustomerProfileTest.getCustomerPaymentProfileId( merchantAuthenticationType, customerProfileId, referenceId);
		String customerShippingAddressId = CustomerShippingAddressTest.createCustomerShippingAddress( merchantAuthenticationType, addressType, customerProfileId, referenceId);

		CustomerProfileIdsHolder customerProfileIdHolder = new CustomerProfileIdsHolder();
		customerProfileIdHolder.CustomerProfileId = customerProfileId;
		customerProfileIdHolder.CustomerPaymentProfileId = customerPaymentProfileId;
		customerProfileIdHolder.CustomerShippingAddressId = customerShippingAddressId;
		
		return customerProfileIdHolder;
	}

	public static void teardownCustomersWithProfile(MerchantAuthenticationType merchantAuthenticationType, CustomerProfileIdsHolder customerProfileHolder, String referenceId) {
		CustomerShippingAddressTest.deleteCustomerShippingAddress(merchantAuthenticationType, customerProfileHolder.CustomerProfileId, customerProfileHolder.CustomerShippingAddressId, referenceId);
		CustomerProfileTest.deleteCustomerProfile( merchantAuthenticationType, customerProfileHolder.CustomerProfileId, referenceId);
	}
	
	class MyTransactionResponse extends TransactionResponse {
		@Override
		public String toString() {
			 StringBuilder builder = new StringBuilder();
			 builder.append("TransactionResponse: ");
			 builder.append(super.toString());

			 builder.append(", ResponseCode: ").append(this.getResponseCode());
			 builder.append(", RawResponseCode: ").append(this.getRawResponseCode());
			 builder.append(", AuthCode: ").append(this.getAuthCode());
			 builder.append(", AvsResultCode: ").append(this.getAvsResultCode());
			 builder.append(", CvvResultCode: ").append(this.getCvvResultCode());
			 builder.append(", CavvResultCode: ").append(this.getCavvResultCode());
			 builder.append(", TransId: ").append(this.getTransId());
			 builder.append(", RefTransID: ").append(this.getRefTransID());
			 builder.append(", TransHash: ").append(this.getTransHash());
			 builder.append(", TestRequest: ").append(this.getTestRequest());
			 builder.append(", AccountNumber: ").append(this.getAccountNumber());
			 builder.append(", AccountType: ").append(this.getAccountType());
			 builder.append(", SplitTenderId: ").append(this.getSplitTenderId());
			 builder.append(", PrePaidCard: ").append(this.getPrePaidCard());
			 builder.append(", Messages: ").append(this.getMessages());
			 builder.append(", Errors: ").append(this.getErrors());
			 builder.append(", SplitTenderPayments: ").append(this.getSplitTenderPayments());
			 builder.append(", UserFields: ").append(this.getUserFields());
			 builder.append(", ShipTo: ").append(this.getShipTo());
			 builder.append(", SecureAcceptance: ").append(this.getSecureAcceptance());

		     return builder.toString();
	   }
	}
	
}
class CustomerProfileIdsHolder 
{
	CustomerProfileIdsHolder(){
	}
	
	CustomerProfileIdsHolder(int customerProfileId, int customerPaymentProfileId, int customerShippingAddressId) {
		if ( customerProfileId > 0) { CustomerProfileId = Integer.toString(customerProfileId);}
		if ( customerPaymentProfileId > 0) { CustomerPaymentProfileId = Integer.toString(customerPaymentProfileId);}
		if ( customerShippingAddressId > 0) { CustomerShippingAddressId = Integer.toString(customerShippingAddressId); }
	}
	
	public String CustomerProfileId;
	public String CustomerPaymentProfileId;
	public String CustomerShippingAddressId;
}
class CustomerProfileElementsHolder {
	
	CustomerProfileElementsHolder() {
		
	}
	CustomerProfileElementsHolder(
			CreateTransactionRequest pCreateRequest, CustomerProfilePaymentType pCustomerProfilePaymentType,
			TransactionRequestType mTransactionRequestType) {
		createRequest = pCreateRequest;
		customerProfilePaymentType = pCustomerProfilePaymentType;
		transactionRequestType = mTransactionRequestType;
	}
	
	public CreateTransactionRequest createRequest;
	public CustomerProfilePaymentType customerProfilePaymentType;
	//public PaymentProfile paymentProfile;
	public TransactionRequestType transactionRequestType;
}