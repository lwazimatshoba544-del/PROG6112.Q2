package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import productapp.ProductCalculations;

public class ProductCalculationsTest {

    @Test
    public void GetVat_WithValidData_ReturnsCorrectValue() {
        ProductCalculations calc = new ProductCalculations(3500);
        assertEquals(525.0, calc.GetVat(), 0.001);
    }

    @Test
    public void GetTotal_WithValidData_ReturnsCorrectValue() {
        ProductCalculations calc = new ProductCalculations(3500);
        assertEquals(4200.0, calc.GetTotal(), 0.001);
    }
}
