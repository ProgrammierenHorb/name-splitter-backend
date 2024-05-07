package com.example.namesplitter.helper;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.apache.logging.log4j.util.Strings;

public class StandardizedSalutationGenerator {

    public static String generateStandardizedSalutation(StructuredName name){
        StringBuilder salutation = new StringBuilder();
        //MALE or FEMALE Gender
        if(name.gender() == Gender.MALE || name.gender() == Gender.FEMALE){
            salutation.append(name.gender() == Gender.MALE ? "Sehr geehrter Herr " : "Sehr geehrte Frau ");
            salutation.append(Strings.join(name.titles(), ' '));
            salutation.append(" ");
            salutation.append(name.lastName());
            return salutation.toString();
        }

        //DIVERSE Gender
        //no firstname given
        if(name.firstName() == null || name.firstName().isBlank()){
            salutation.append("Sehr geehrte Damen und Herren");
            return salutation.toString();
        }

        //firstname given
        salutation.append("Guten Tag ");
        salutation.append(name.firstName());
        salutation.append(" ");
        salutation.append(name.lastName());
        return salutation.toString();
    }
}
