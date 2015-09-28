package com.development.buccola.myrecipes.common;
/***************************************************
 * FILE:        UnitConversions
 * PROGRAMMER:  Megan Buccola
 * CREATED:     4/14/15
 * PURPOSE:     Convert unit amounts
 ***************************************************/

public class UnitConversions {
    private String dbUnit;
    private String selected;
    private double amt;

    /*********************************
     FUNCTION:  UnitConversions(String, from, double)
     PARAMS:    String - the unit being converted to, String - the unit being converted from, double - the amount being converted
     RETURNS:   Nothing
     PURPOSE:   constructor
     *********************************/
    public UnitConversions(String to, String from, double amt_){
        dbUnit = to.trim().toLowerCase();
        selected = from.trim().toLowerCase();
        amt = amt_;
    }
    /*********************************
     FUNCTION:  double compareUnits()
     PARAMS:    NONE
     RETURNS:   double
     PURPOSE:   Returns the new amount
     *********************************/
    public double compareUnits(){
        switch(selected.toLowerCase()){
            case "cups":
            case "cup(s)":
            	return convertCupsTo(dbUnit, amt);
            case "gallons":
            case "gallon(s)":
            	return convertGallonsTo(dbUnit, amt);
            case "quart":
            case "quarts":
            case "quart(s)":
                return convertQuartsTo(dbUnit, amt);
            case "ounces":
            case "ounce(s)":
                return convertOuncesTo(dbUnit, amt);
            case "tsp":
                return convertTSPTo(dbUnit, amt);
            case "tbsp":
                return convertTBSPTo(dbUnit, amt);
            default:
                return -1;
        }
    }

    /*********************************
     FUNCTION:  double convertCupsTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double - the result/new amount
     PURPOSE:   get the amount when converting cups to unitTo
     *********************************/
    public double convertCupsTo(String unitTo, double requiredAmount){
        //convert requiredAmount of cups to ___ dbUnits
        final double oneCupinGal = 0.0625;
        final double oneCupinQuarts = 0.25;
        final double oneCupinTsp = 48;
        final double oneCupinTbsp = 16;
        final double oneCupinOz = 8;
        
        switch(unitTo.toLowerCase()){
        	case "gallon(s)":
            case "gallons":
            	return (requiredAmount *1)*oneCupinGal;
            case "tsp":
                return (requiredAmount*1)*oneCupinTsp;
            case "tbsp":
                return (requiredAmount*1)*oneCupinTbsp;
            case "cup(s)":
            case "cups":
                return (requiredAmount*1)*1;
            case "quart":
            case "quart(s)":
            case "quarts":
                return (requiredAmount*1)*oneCupinQuarts;
            case "ounce(s)":
            case "ounces":
                return (requiredAmount*1)*oneCupinOz;
            default:
                return -1;
        }

    }
    /*********************************
     FUNCTION:  double convertGallonsTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double - the result/ new amount
     PURPOSE:   get the amount when converting gallons to unitTo
     *********************************/
    public double convertGallonsTo(String unitTo,  double requiredAmount){
        final double oneGalinCups = 16;
        final double oneGalinQuarts = 4;
        final double oneGalinTsp = 768;
        final double oneGalinTbsp = 256;
        final double oneGalinOz = 128;

        switch(unitTo){
        	case "cup(s)":
            case "cups":
            	return (requiredAmount *1)*oneGalinCups;
            case "tsp":
                return (requiredAmount*1)*oneGalinTsp;
            case "tbsp":
                return (requiredAmount*1)*oneGalinTbsp;
            case "quart(s)":
            case "quart":
            case "quarts":
                return (requiredAmount*1)*oneGalinQuarts;
            case "ounce(s)":
            case "ounces":
                return (requiredAmount*1)*oneGalinOz;
            case "gallon(s)":
            case "gallon":
            case "gallons":
                return requiredAmount;
            default:
                return -1;
        }

    }

    /*********************************
     FUNCTION:  double convertQuartsTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double - result / new Amount
     PURPOSE:   get the amount when converting cups to unitTo
     *********************************/
    public double convertQuartsTo(String unitTo,  double requiredAmount){
        final double oneQuartinGal = 0.25;
        final double oneQuartinCups = 4;
        final double oneQuartinTsp = 192;
        final double oneQuartinTbsp = 64;
        final double oneQuartinOz = 32;
        switch(unitTo){
        	case "gallon(s)":
        	case "gallon":
        	case "gallons":
        		return (requiredAmount *1)*oneQuartinGal;
            case "tsp":
                return (requiredAmount*1)*oneQuartinTsp;
            case "tbsp":
                return (requiredAmount*1)*oneQuartinTbsp;
            case "cup(s)":
            case "cups":
                return (requiredAmount*1)*oneQuartinCups;
            case "ounce(s)":
            case "ounces":
                return (requiredAmount*1)*oneQuartinOz;
            case "quart(s)":
            case "quart":
            case "quarts":
                return requiredAmount;
            default:
                return -1; //error
        }
    }

    /*********************************
     FUNCTION:  double convertOuncesTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double result / new Amount
     PURPOSE:   get the amount when converting ounces to unitTo
     *********************************/
    public double convertOuncesTo(String dbUnit,  double requiredAmount){
        final double oneOzinGal = 0.0078125;
        final double oneOzinQuarts = 0.03125;
        final double oneOzinTsp = 6;
        final double oneOzinTbsp = 2;
        final double oneOzinCups = 0.125;

        switch(dbUnit){
        	case "gallon":
        	case "gallon(s)":
            case "gallons":
                return (requiredAmount *1)*oneOzinGal;
            case "tsp":
                return (requiredAmount*1)*oneOzinTsp;
            case "tbsp":
                return (requiredAmount*1)*oneOzinTbsp;
            case "quart(s)":
            case "quart":
            case "quarts":
                return (requiredAmount*1)*oneOzinQuarts;
            case "cup(s)":
            case "cups":
                return (requiredAmount*1)*oneOzinCups;
            case "ounce(s)":
            case "ounces":
                return requiredAmount;
            default:
                return -1;
        }
    }
    /*********************************
     FUNCTION:  double convertTSPTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double result / new Amount
     PURPOSE:   get the amount when converting tsp to unitTo
     *********************************/
    public double convertTSPTo(String unitTo,  double requiredAmount){
        final double oneTspinGal = 0.00130208;
        final double oneTspinQuarts = 0.00520833;
        final double oneTspinOz = 0.166667;
        final double oneTspinTbsp = 0.333333;
        final double oneTspinCups = 0.0208333;

        switch(unitTo){
        	case "gallon":
        	case "gallon(s)":
            case "gallons":
                return (requiredAmount *1)*oneTspinGal;
            case "tsp":
                return (requiredAmount*1);
            case "tbsp":
                return (requiredAmount*1)*oneTspinTbsp;
            case "quart":
            case "quart(s)":
            case "quarts":
                return (requiredAmount*1)*oneTspinQuarts;
            case "cup(s)":
            case "cups":
                return (requiredAmount*1)*oneTspinCups;
            case "ounce(s)":
            case "ounces":
                return (requiredAmount*1)*oneTspinOz;
            default:
                return -1;
        }
    }

    /*********************************
     FUNCTION:  double convertTBSPTo(String, double)
     PARAMS:    String - unit being converted to; double amount given
     RETURNS:   double result / new Amount
     PURPOSE:   get the amount when converting tbsp to unitTo
     *********************************/
    public double convertTBSPTo(String unitTo,  double requiredAmount){
        final double oneTbspinGal = 0.00390625;
        final double oneTbspinQuarts = 0.015625;
        final double oneTbspinTsp = 3;
        final double oneTbspinOz = 0.5;
        final double oneTbspinCups = 0.0625;

        switch(unitTo){
        	case "gallon(s)":
        	case "gallon":
        	case "gallons":
                return (requiredAmount *1)*oneTbspinGal;
            case "tsp":
                return (requiredAmount*1)*oneTbspinTsp;
            case "ounce(s)":
            case "ounces":
                return (requiredAmount*1)*oneTbspinOz;
            case "quart(s)":
            case "quart":
            case "quarts":
                return (requiredAmount*1)*oneTbspinQuarts;
            case "cup(s)":
            case "cups":
                return (requiredAmount*1)*oneTbspinCups;
            case "tbsp":
                return requiredAmount;
            default:
                return -1;
        }
    }
}
