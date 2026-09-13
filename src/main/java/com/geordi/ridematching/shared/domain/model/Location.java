package com.geordi.ridematching.shared.domain.model;

public record Location(double x, double y) {

    public static Location create(Double x, Double y) {
		if (x == null || y == null) {
			throw new IllegalArgumentException("x and y are required");
		}
		return new Location(x, y);
	}

	public double distanceTo(Location other) {
		double deltaX = this.x - other.x;
		double deltaY = this.y - other.y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
}