package fr.uge.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarRental {
	private final ArrayList<Drivable> carRental;

	public CarRental() {
		this.carRental = new ArrayList<Drivable>();
	}

	//	@Override
	//	public String toString() {
	//		var sb = new StringBuilder();
	//		int i = 0;
	//		for (Drivable drivable : carRental) {
	//			if(i != 0) {
	//				sb.append('\n');	
	//			}
	//			sb.append(drivable);
	//			i++;
	//		}
	//		return sb.toString();
	//	}

	@Override
	public String toString() {
		return carRental.stream().map(d -> d.toString()).collect(Collectors.joining("\n"));
	}
	public void add(Drivable drivable) {
		Objects.requireNonNull(drivable);
		carRental.add(drivable);
	}

	public void remove(Drivable drivable) {
		Objects.requireNonNull(drivable);
		if(!carRental.remove(drivable)) {
			throw new IllegalStateException("this car is not in carRental");			
		}
	}

	public List<Drivable> findAllByYear(int year){
		return carRental.stream().filter(d -> d.year() == year).toList();
	}

	public int insuranceCostAt(int year) {
		return carRental.stream().mapToInt(d -> d.insuranceCostAt(year)).sum();
	}

	//		public Optional<Car> findACarByModel(String model){
	//			if(model == null) {
	//				throw new NullPointerException();
	//			}
	//			for (Drivable drivable : carRental) {
	//				if(drivable instanceof Car car && car.model().equals(model)) {
	//					return Optional.of(car);
	//				}
	//			}
	//			return Optional.empty();
	//		}

	public Optional<Car> findACarByModel(String model){
		Objects.requireNonNull(model);
		return carRental
				.stream()
				.flatMap(
						d -> {
							return switch(d) {
							case Car car  && car.model().equals(model) -> Stream.of(car);
							case Car car  && !car.model().equals(model) -> Stream.empty();
							case Camel camel -> Stream.empty();
							};
						}
						).findFirst();
	}
}
