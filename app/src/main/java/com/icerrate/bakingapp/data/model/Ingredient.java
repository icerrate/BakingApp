package com.icerrate.bakingapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ivan Cerrate.
 */

public class Ingredient implements Parcelable {

    private Double quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        switch(measure){
            case "CUP":
                return quantity > 1 ? "cups" : "cup";
            case "G":
                return quantity > 1 ? "grams" : "gram";
            case "K":
                return quantity > 1 ? "kilos" : "kilo";
            case "OZ":
                return quantity > 1 ? "ounces" : "ounce";
            case "TBLSP":
                return quantity > 1 ? "tablespoons" : "tablespoon";
            case "TSP":
                return quantity > 1 ? "teaspoons" : "teaspoon";
            case "UNIT":
                return quantity > 1 ? "units" : "unit";
            default:
                return measure;
        }
    }

    public String getIngredient() {
        String stylishIngredient = Character.toUpperCase(
                ingredient.charAt(0)) + ingredient.substring(1);
        return stylishIngredient;
    }

    public Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
