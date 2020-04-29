package nl.cookplanner.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UpdatePlanDates {

	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;

	@Override
	public String toString() {
		return "\nUpdatePlanDates [id=" + id + ", date=" + date + "]";
	}
	
	
}
