package fr.uge.tp0;

public record Camel(int year) implements Drivable {
	
	public Camel {
		if(year < 0)
			throw new IllegalStateException("Invalid year");
	}
	
	@Override
	public String toString() {
		return "camel " + year;		
	}
	
	@Override
	public int insuranceCostAt(int year) {
		if(year < this.year) {
			throw new IllegalArgumentException("year < this.year");
		}
		return (year - this.year) * 100;
	}
}
