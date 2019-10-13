package com.lendico.plangenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendico.plangenerator.model.LoanInfoRequest;
import com.lendico.plangenerator.service.PlanGeneratorService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoanRepaymentPlanGeneratorApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	PlanGeneratorService planGeneratorService;

	@Test
	public void testNextRepaymentDate() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date expectedDate = dateFormat.parse("2018-02-01T00:00:00Z");
		Date actualDate = planGeneratorService.getNextRepaymentDate(dateFormat.parse("2018-01-01T00:00:00Z"));
		assertThat(expectedDate).isEqualTo(actualDate);
	}

	@Test
	public void testMonthlyInterest() throws Exception {
		BigDecimal expectedMonthlyInterest = new BigDecimal("20.83");
		BigDecimal actualMonthlyInterest = planGeneratorService.getMonthlyInterest(new BigDecimal("5000"),
				new BigDecimal("5.0").divide(new BigDecimal(100)));
		assertThat(expectedMonthlyInterest).isEqualTo(actualMonthlyInterest);
	}

	@Test
	public void testAnnuityPayment() throws Exception {
		BigDecimal expectedAnnuityPayment = new BigDecimal("219.36");
		BigDecimal actualAnnuityPayment = planGeneratorService.getAnnuityPayment(new BigDecimal("5000"),
				new BigDecimal("5.0").divide(new BigDecimal(1200), 8, RoundingMode.HALF_UP), 24);
		assertThat(expectedAnnuityPayment).isEqualTo(actualAnnuityPayment);
	}

	@Test
	public void whenNullLoan_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("", "5.0", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.loanAmount").value("Loan Amount may not be null")).andReturn();
	}
	
	@Test
	public void whenZeroLoan_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("0", "5.0", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.loanAmount").value("Loan Amount must be a positive number")).andReturn();
	}
	
	@Test
	public void whenNegitiveLoan_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("-5000", "5.0", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.loanAmount").value("Loan Amount must be a positive number")).andReturn();
	}

	@Test
	public void whenNullNominalRate_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.nominalRate").value("Nominal Rate may not be null")).andReturn();
	}
	
	@Test
	public void whenZeroNominalRate_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "0", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.nominalRate").value("Normal Rate must be a positive number")).andReturn();
	}
	
	@Test
	public void whenNegitiveNominalRate_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "-5", 24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.nominalRate").value("Normal Rate must be a positive number")).andReturn();
	}

	@Test
	public void whenNullDuration_thenOneConstrainViolation() throws Exception {

		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "5.0", "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.duration").value("Duration may not be null"))
				.andReturn();
	}
	
	@Test
	public void whenZeroDuration_thenOneConstrainViolation() throws Exception {

		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "5.0", 0 ,"2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.duration").value("Duration must be a positive number"))
				.andReturn();
	}
	
	@Test
	public void whenNegitiveDuration_thenOneConstrainViolation() throws Exception {

		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "5.0", -24, "2018-01-01T01:00:01Z"))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.duration").value("Duration must be a positive number"))
				.andReturn();
	}

	@Test
	public void whenNullStartDate_thenOneConstrainViolation() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "5.0", 24, ""))))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.startDate").value("Start Date may not be null")).andReturn();
	}

	@Test
	public void whenStartDateFormatterWrong_thenParseException() throws Exception {
		mockMvc.perform(post("/api/generateRepaymentPlan").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(new LoanInfoRequest("5000", "5.0", 24, "2018-01-01"))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorCode").value("1001"))
				.andExpect(jsonPath("$.errorDesc").value("Unparseable date: \"2018-01-01\"")).andReturn();
	}


}
