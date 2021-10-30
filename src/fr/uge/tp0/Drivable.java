package fr.uge.tp0;

public sealed interface Drivable permits Car, Camel {
	int year();
	int insuranceCostAt(int year);
}
