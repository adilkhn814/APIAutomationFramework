package com.dataprovider;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.utility.TestUtility;

public class GetProfileDataprovider {
	
	@DataProvider(name = "User Data")
	public Iterator<String[]> UserProfileDB() {

		return TestUtility.readCSV("getProfileData.csv");

}}





