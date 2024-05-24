package com.coderscampus.assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileService {

	public List<String> readFile(String fileName) {
		List<String> numbers = new ArrayList<>();

		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			while ((line = reader.readLine()) != null) {
				numbers.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numbers;
	}
}
