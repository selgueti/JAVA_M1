package fr.uge.tp0;

import java.util.Objects;

public record Car(String model, int year) implements Drivable {

	public Car {
		Objects.requireNonNull(model);
		if(year < 0)
			throw new IllegalStateException("Invalid year");
	}
	
	@Override
	public String toString() {
		return model + " " + year;		
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Car car && model.equals(car.model()) && year == car.year();
	}
	
	@Override
	public int hashCode() {
		return model.hashCode() ^ year;
	}
	
	@Override
	public int insuranceCostAt(int year) {
		if(year < this.year) {
			throw new IllegalArgumentException("year < this.year");
		}
		return (year - this.year) < 10 ? 200 : 500;
	}
	
}
