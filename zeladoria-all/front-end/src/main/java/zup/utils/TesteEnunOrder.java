package zup.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import zup.enums.InformationType;

public class TesteEnunOrder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<InformationType> list= new ArrayList<InformationType>();
		for(InformationType ot:InformationType.values())
			list.add(ot);
		list.sort(new Comparator<InformationType>() {
		public int compare(InformationType i1,InformationType i2){
			return i1.getStr().compareTo(i2.getStr());
		};

			
		});
		for(InformationType i:list)
			System.out.println(i.getStr());
	}

}
