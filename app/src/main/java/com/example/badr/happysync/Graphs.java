package com.example.badr.happysync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector; 

public class Graphs { 


    private int v; 
	

    private ArrayList<Integer>[] adjList; 
	Vector <String> solutions = new Vector();
	Vector <String> TheSolutions = new Vector();

	HashMap<String,String> ReseauMetro = new HashMap<String, String>();
	HashMap<String,String> ReseauBus = new HashMap<String, String>();



    public Graphs(int vertices){
        this.v = vertices; 

		initAdjList(); 
		addEdge(0,18); // Tunis -> manar (metro 5/3)
		addEdge(0,39); //tunis -> beb sadoun
		addEdge(0,25); // tunis -> cite etadhamen
		addEdge(18,0); //manar -> tunis
		addEdge(18,39); // MANAR -> BEB SAADOUN
		addEdge(18,25); //MANAR -> CITE ETADHAMEN
		addEdge(39,0); //BEB SAADOUN -> TUNIS
		addEdge(39,18);//BEB SAADOUN -> MANAR
		addEdge(39,25); // BEB SAADOUN -> CITE ETADHAMEN
		addEdge(25,0); //CITE ETADHAMEN -> TUNIS
		addEdge(25,39); // CITE ETADHAMEN -> BEB SAADOUN
		addEdge(25,18); // CITE ETADHEMEN -> MANAR
		addEdge(0,1); // TUNIS -> ARIANA
		addEdge(0,27); // TUNIS -> CITE KHADHRA
		addEdge(1,0); // ARIANA -> TUNIS
		addEdge(1,27); // ARIANA -> CITE KHADHRA
		addEdge(27,0); // CITE KHADHRA -> TUNIS
		addEdge(27,1); // CITE KHADHRA -> ARIANA
		addEdge(0,3);//TUNIS -> MANOUBA
		addEdge(0,19);//TUNIS -> BARDO
		addEdge(0,20);//TUNIS -> DANDEN
		addEdge(0,23);//TUNIS -> KHAZNADAR
		addEdge(3,0);//MANOUBA -> TUNIS
		addEdge(3,19);//MANOUBA -> BARDO
		addEdge(3,23);//MANOUBA -> KHAZNADAR
		addEdge(3,20);//MANOUBA -> DANDEN
		addEdge(20,3);//DANDEN -> MANOUBA
		addEdge(20,0);//DANDEN -> TUNIS
		addEdge(20,19);//DANDEN -> BARDO
		addEdge(20,23);//DANDEN -> KHAZNADAR
		addEdge(23,0);//KHAZNADAR -> TUNIS
		addEdge(23,3);//KHAZNADAR -> MANOUBA
		addEdge(23,19);//KHAZNADAR -> BARDO
		addEdge(23,20);//KHAZNADAR -> DANDEN
		addEdge(19,0);//BARDO -> TUNIS
		addEdge(19,3);// BARDO -> MANOUBA
		addEdge(19,23);// BARDO -> KHAZNADAR
		addEdge(19,20);// BARDO -> DANDEN
		addEdge(45,0);// IBN KHALDOUN -> TUNIS
		addEdge(45,18);// IBN KHALDOUN -> MANAR
		addEdge(45,39);// IBN KHALDOUN -> BEB SAADOUN
		addEdge(0,45);// TUNIS -> IBN KHALDOUN
		addEdge(18,45);// MANAR -> IBN KHALDOUN
		addEdge(39,45);// BEB SA3DOUN -> IBN KHALDOUN
		addEdge(0,29);// TUNIS -> MOUROUJ
		addEdge(29,0); // MOUROUJ -> TUNIS
		addEdge(4,0); // BEN AROUS -> TUNIS
		addEdge(40,0); // KABARIA -> TUNIS
		addEdge(0,4);// TUNIS -> BEN AROUS
		addEdge(0,40);// TUNIS -> KABARIA 
		addEdge(4,40);// BEN AROUS -> KABARIA
		addEdge(40,4);// KABARIA -> BEN AROUS
		addEdge(46,30); // TUNIS BARCELONE -> BORJ CEDRIA
		addEdge(46,31); // TUNIS BARCELONE-> RADES
		addEdge(46,32); // TUNIS BARCELONE -> MEGRINE
		addEdge(30,46); // BORJ CEDRIA -> TUNIS BARCELONE
		addEdge(30,31);// BORJ CEDRIA -> RADES
		addEdge(30,32);// BORJ CEDRIA -> MEGRINE
		addEdge(31,46);// RADES -> TUNIS BARCELONE
		addEdge(31,30); // RADES -> BORJ CEDRIA
		addEdge(31,32);// RADES -> MEGRINE
		addEdge(32,46);// MEGRINE -> TUNIS BARCELONE
		addEdge(32,30);// MEGRINE -> BORJ CEDRIA
		addEdge(32,31); // MEGRINE -> RADES
		addEdge(46,0); // BARCELONE -> PASSAGE
		addEdge(0,46); // PASSAGE -> BARCELONE
		addEdge(47,46); // TGM -> BARCELONE
		addEdge(46,47); // BARCELONE -> TGM
		addEdge(47,4);
		addEdge(47,5);
		addEdge(47,6);
		addEdge(47,10);
		addEdge(47,11);
		addEdge(47,44);
		addEdge(4,47);
		addEdge(4,5);
		addEdge(4,6);
		addEdge(4,10);
		addEdge(4,11);
		addEdge(4,44);
		addEdge(5,47);
		addEdge(5,4);
		addEdge(5,6);
		addEdge(5,10);
		addEdge(5,11);
		addEdge(5,44);
		addEdge(6,47);
		addEdge(6,4);
		addEdge(6,5);
		addEdge(6,10);
		addEdge(6,11);
		addEdge(6,44);
		addEdge(10,47);
		addEdge(10,5);
		addEdge(10,6);
		addEdge(10,11);
		addEdge(10,4);
		addEdge(10,44);
		addEdge(11,47);
		addEdge(11,4);
		addEdge(11,5);
		addEdge(11,6);
		addEdge(11,10);
		addEdge(11,44);
		addEdge(44,47);
		addEdge(44,4);
		addEdge(44,5);
		addEdge(44,6);
		addEdge(44,10);
		addEdge(44,11);

		//** Bus
		addEdge(19,25);
		addEdge(25,19);
		addEdge(0,36);
		addEdge(36,0);
		addEdge(19,36);
		addEdge(36,19);
		addEdge(1,47);
		addEdge(1,34);//ARIANA -> GHAZELA
		addEdge(47,1);
		addEdge(47,34);
		addEdge(34,47);
		addEdge(34,1);//GHAZELA -> ARIANA
		addEdge(1,12);
		addEdge(1,14);// ARIANA -> M5
		addEdge(12,1);
		addEdge(12,14);
		addEdge(14,1);// ARIANA -> M5
		addEdge(14,12);
		addEdge(0,34);
		addEdge(34,0);
		addEdge(35,1);
		addEdge(35,14);
		addEdge(35,34);
		addEdge(35,7);
		addEdge(7,1);
		addEdge(7,14);
		addEdge(7,34);
		addEdge(7,35);
		addEdge(1,35);
		addEdge(1,7);
		addEdge(14,34);
		addEdge(14,35);
		addEdge(14,7);
		addEdge(34,14);
		addEdge(34,35);
		addEdge(34,7);
		addEdge(1,6);
		addEdge(1,22);
		addEdge(14,6);
		addEdge(14,22);
		addEdge(6,1);
		addEdge(6,14);
		addEdge(6,22);
		addEdge(22,1);
		addEdge(22,14);
		addEdge(22,6);
		addEdge(4,1);
		addEdge(4,22);
		addEdge(4,14);
		addEdge(1,4);
		addEdge(22,4);
		addEdge(14,4);



		/** Metro 5 */
		ReseauMetro.put("0-18","Metro 5/Metro 3"); // Tunis -> manar (metro 5/3)
		ReseauMetro.put("0-39","Metro 5/Metro 4/Metro 3"); //tunis -> beb sadoun
		ReseauMetro.put("0-25","Metro 5"); // tunis -> cite etadhamen
		ReseauMetro.put("18-0","Metro 5/Metro 3"); //manar -> tunis
		ReseauMetro.put("18-39","Metro 5/Metro 3"); // MANAR -> BEB SAADOUN
		ReseauMetro.put("18-25","Metro 5"); //MANAR -> CITE ETADHAMEN
		ReseauMetro.put("39-0","Metro 5/Metro 4/Metro 3"); //BEB SAADOUN -> TUNIS
		ReseauMetro.put("39-18","Metro 5/Metro 3");//BEB SAADOUN -> MANAR
		ReseauMetro.put("39-25","Metro 5"); // BEB SAADOUN -> CITE ETADHAMEN
		ReseauMetro.put("25-0","Metro 5"); //CITE ETADHAMEN -> TUNIS
		ReseauMetro.put("25-39","Metro 5"); // CITE ETADHAMEN -> BEB SAADOUN
		ReseauMetro.put("25-18","Metro 5"); // CITE ETADHEMEN -> MANAR

		/** Metro 2 */
		ReseauMetro.put("0-1","Metro 2"); // TUNIS -> ARIANA
		ReseauMetro.put("0-27","Metro 2"); // TUNIS -> CITE KHADHRA
		ReseauMetro.put("1-0","Metro 2"); // ARIANA -> TUNIS
		ReseauMetro.put("1-27","Metro 2"); // ARIANA -> CITE KHADHRA
		ReseauMetro.put("27-0","Metro 2"); // CITE KHADHRA -> TUNIS
		ReseauMetro.put("27-1","Metro 2"); // CITE KHADHRA -> ARIANA



		/** Metro 4 */

		ReseauMetro.put("0-3","Metro 4");//TUNIS -> MANOUBA
		ReseauMetro.put("0-19","Metro 4");//TUNIS -> BARDO
		ReseauMetro.put("0-20","Metro 4");//TUNIS -> DANDEN
		ReseauMetro.put("0-23","Metro 4");//TUNIS -> KHAZNADAR
		ReseauMetro.put("3-0","Metro 4");//MANOUBA -> TUNIS
		ReseauMetro.put("3-19","Metro 4");//MANOUBA -> BARDO
		ReseauMetro.put("3-23","Metro 4");//MANOUBA -> KHAZNADAR
		ReseauMetro.put("3-20","Metro 4");//MANOUBA -> DANDEN
		ReseauMetro.put("20-3","Metro 4");//DANDEN -> MANOUBA
		ReseauMetro.put("20-0","Metro 4");//DANDEN -> TUNIS
		ReseauMetro.put("20-19","Metro 4");//DANDEN -> BARDO
		ReseauMetro.put("20-23","Metro 4");//DANDEN -> KHAZNADAR
		ReseauMetro.put("23-0","Metro 4");//KHAZNADAR -> TUNIS
		ReseauMetro.put("23-3","Metro 4");//KHAZNADAR -> MANOUBA
		ReseauMetro.put("23-19","Metro 4");//KHAZNADAR -> BARDO
		ReseauMetro.put("23-20","Metro 4");//KHAZNADAR -> DANDEN
		ReseauMetro.put("19-0","Metro 4");//BARDO -> TUNIS
		ReseauMetro.put("19-3","Metro 4");// BARDO -> MANOUBA
		ReseauMetro.put("19-23","Metro 4");// BARDO -> KHAZNADAR
		ReseauMetro.put("19-20","Metro 4");// BARDO -> DANDEN

		/** Metro 3 */
		ReseauMetro.put("45-0","Metro 3");// IBN KHALDOUN -> TUNIS
		ReseauMetro.put("45-18","Metro 3");// IBN KHALDOUN -> MANAR
		ReseauMetro.put("45-39","Metro 3");// IBN KHALDOUN -> BEB SAADOUN
		ReseauMetro.put("0-45","Metro 3");// TUNIS -> IBN KHALDOUN
		ReseauMetro.put("18-45","Metro 3");// MANAR -> IBN KHALDOUN
		ReseauMetro.put("39-45","Metro 3");// BEB SA3DOUN -> IBN KHALDOUN

		/** Metro 6 */
		ReseauMetro.put("0-29","Metro 6");// TUNIS -> MOUROUJ
		ReseauMetro.put("29-0","Metro 6"); // MOUROUJ -> TUNIS

		/** Metro 1 */
		ReseauMetro.put("2-0","Metro 1"); // BEN AROUS -> TUNIS
		ReseauMetro.put("40-0","Metro 1"); // KABARIA -> TUNIS
		ReseauMetro.put("0-2","Metro 1");// TUNIS -> BEN AROUS
		ReseauMetro.put("0-40","Metro 1");// TUNIS -> KABARIA
		ReseauMetro.put("2-40","Metro 1");// BEN AROUS -> KABARIA
		ReseauMetro.put("40-2","Metro 1");// KABARIA -> BEN AROUS

		/** Train SNCFT */
		ReseauMetro.put("46-30","Train"); // TUNIS BARCELONE -> BORJ CEDRIA
		ReseauMetro.put("46-31","Train"); // TUNIS BARCELONE-> RADES
		ReseauMetro.put("46-32","Train"); // TUNIS BARCELONE -> MEGRINE
		ReseauMetro.put("30-46","Train"); // BORJ CEDRIA -> TUNIS BARCELONE
		ReseauMetro.put("30-31","Train");// BORJ CEDRIA -> RADES
		ReseauMetro.put("30-32","Train");// BORJ CEDRIA -> MEGRINE
		ReseauMetro.put("31-46","Train");// RADES -> TUNIS BARCELONE
		ReseauMetro.put("31-30","Train"); // RADES -> BORJ CEDRIA
		ReseauMetro.put("31-32","Train");// RADES -> MEGRINE
		ReseauMetro.put("32-46","Train");// MEGRINE -> TUNIS BARCELONE
		ReseauMetro.put("32-30","Train");// MEGRINE -> BORJ CEDRIA
		ReseauMetro.put("32-31","Train"); // MEGRINE -> RADES

		/** Metro 4/5/3 */
		ReseauMetro.put("46-0","Metro 5/4/3"); // BARCELONE -> PASSAGE
		ReseauMetro.put("0-46","Metro 5/4/3"); // PASSAGE -> BARCELONE

		/** Metro 6/1 */
		ReseauMetro.put("47-46","Metro 6/1"); // TGM -> BARCELONE
		ReseauMetro.put("46-47","Metro 6/1"); // BARCELONE -> TGM

		/** TGM */
		ReseauMetro.put("47-4","TGM");
		ReseauMetro.put("47-5","TGM");
		ReseauMetro.put("47-6","TGM");
		ReseauMetro.put("47-10","TGM");
		ReseauMetro.put("47-11","TGM");
		ReseauMetro.put("47-44","TGM");
		ReseauMetro.put("4-47","TGM");
		ReseauMetro.put("4-5","TGM");
		ReseauMetro.put("4-6","TGM");
		ReseauMetro.put("4-10","TGM");
		ReseauMetro.put("4-11","TGM");
		ReseauMetro.put("4-44","TGM");
		ReseauMetro.put("5-47","TGM");
		ReseauMetro.put("5-4","TGM");
		ReseauMetro.put("5-6","TGM");
		ReseauMetro.put("5-10","TGM");
		ReseauMetro.put("5-11","TGM");
		ReseauMetro.put("5-44","TGM");
		ReseauMetro.put("6-47","TGM");
		ReseauMetro.put("6-4","TGM");
		ReseauMetro.put("6-5","TGM");
		ReseauMetro.put("6-10","TGM");
		ReseauMetro.put("6-11","TGM");
		ReseauMetro.put("6-44","TGM");
		ReseauMetro.put("10-47","TGM");
		ReseauMetro.put("10-5","TGM");
		ReseauMetro.put("10-6","TGM");
		ReseauMetro.put("10-11","TGM");
		ReseauMetro.put("10-4","TGM");
		ReseauMetro.put("10-44","TGM");
		ReseauMetro.put("11-47","TGM");
		ReseauMetro.put("11-4","TGM");
		ReseauMetro.put("11-5","TGM");
		ReseauMetro.put("11-6","TGM");
		ReseauMetro.put("11-10","TGM");
		ReseauMetro.put("11-44","TGM");
		ReseauMetro.put("44-47","TGM");
		ReseauMetro.put("44-4","TGM");
		ReseauMetro.put("44-5","TGM");
		ReseauMetro.put("44-6","TGM");
		ReseauMetro.put("44-10","TGM");
		ReseauMetro.put("44-11","TGM");

		/** Bus 68 */
		ReseauBus.put("0-19","Bus 68/56/55");
		ReseauBus.put("0-25","Bus 68");
		ReseauBus.put("19-0","Bus 68/56/55");
		ReseauBus.put("19-25","Bus 68");
		ReseauBus.put("25-19","Bus 68");
		ReseauBus.put("25-0","Bus 68");

		/** Bus 55 */
		ReseauBus.put("36-0","Bus 55");
		ReseauBus.put("36-19","Bus 55");
		ReseauBus.put("0-36","Bus 55");
		ReseauBus.put("19-36","Bus 55");


		/** Bus 77 */
		ReseauBus.put("47-1","Bus 77");
		ReseauBus.put("47-34","Bus 77");
		ReseauBus.put("1-34","Bus 77/27/27C/62");
		ReseauBus.put("1-47","Bus 77");
		ReseauBus.put("34-47","Bus 77");
		ReseauBus.put("34-1","Bus 77/27/27C/62");

		/** Bus 6 */
		ReseauBus.put("1-12","Bus 6");
		ReseauBus.put("1-14","Bus 6/27/27C/18C/18/62");
		ReseauBus.put("12-1","Bus 6");
		ReseauBus.put("12-14","Bus 6");
		ReseauBus.put("14-1","Bus 6/27/27C/18C/18/62");
		ReseauBus.put("14-12","Bus 6");

		/** Bus 527 */
		ReseauBus.put("0-34","Bus 527");
		ReseauBus.put("34-0","Bus 527");


		/** Bus 27 */
		ReseauBus.put("35-1","Bus 27/27C");
		ReseauBus.put("35-14","Bus 27/27C");
		ReseauBus.put("35-34","Bus 27/27C");
		ReseauBus.put("35-7","Bus 27/27C");
		ReseauBus.put("7-1","Bus 27/27C");
		ReseauBus.put("7-14","Bus 27/27C");
		ReseauBus.put("7-34","Bus 27/27C");
		ReseauBus.put("7-35","Bus 27/27C");
		ReseauBus.put("1-35","Bus 27/27C");
		ReseauBus.put("1-7","Bus 27/27C");
		ReseauBus.put("14-34","Bus 27/27C/62");
		ReseauBus.put("14-35","Bus 27/27C");
		ReseauBus.put("14-7","Bus 27/27C");
		ReseauBus.put("34-14","Bus 27/27C/62");
		ReseauBus.put("34-35","Bus 27/27C");
		ReseauBus.put("34-7","Bus 27/27C");

		/** Bus 18C  */
		ReseauBus.put("1-6","Bus 18C");
		ReseauBus.put("1-22","Bus 18C/18/18B");
		ReseauBus.put("14-6","Bus 18C");
		ReseauBus.put("14-22","Bus 18C/18");
		ReseauBus.put("6-1","Bus 18C");
		ReseauBus.put("6-14","Bus 18C");
		ReseauBus.put("6-22","Bus 18C");
		ReseauBus.put("22-1","Bus 18C/18/18B");
		ReseauBus.put("22-14","Bus 18C/18");
		ReseauBus.put("22-6","Bus 18C");

		/** Bus 18 */
		ReseauBus.put("4-1","Bus 18");
		ReseauBus.put("4-22","Bus 18");
		ReseauBus.put("4-14","Bus 18");
		ReseauBus.put("1-4","Bus 18");
		ReseauBus.put("22-4","Bus 18");
		ReseauBus.put("14-4","Bus 18");

	} 
	
	@SuppressWarnings("unchecked") 
	private void initAdjList() 
	{ 
		adjList = new ArrayList[v]; 
		
		for(int i = 0; i < v; i++) 
		{ 
			adjList[i] = new ArrayList<>(); 
		} 
	} 
	
	public void addEdge(int u, int v) 
	{ 
		adjList[u].add(v); 
	} 
	
	public void printAllPaths(int s, int d) 
	{ 
		boolean[] isVisited = new boolean[v]; 
		ArrayList<Integer> pathList = new ArrayList<>(); 
		
		pathList.add(s); 
		
		printAllPathsUtil(s, d, isVisited, pathList); 
	} 

	private void printAllPathsUtil(Integer u, Integer d, 
									boolean[] isVisited, 
							List<Integer> localPathList) { 
		
		isVisited[u] = true; 
		
		if (u.equals(d)) 
		{ 
			//System.out.println(localPathList); 
			String ch = localPathList.get(0)+"";
			for (int i = 1 ; i< localPathList.size() ; i++){
				ch = ch +"-"+localPathList.get(i);
			}
			solutions.add(ch);
			isVisited[u]= false; 
			return ; 
		} 
		
		for (Integer i : adjList[u]) 
		{ 
			if (!isVisited[i]) 
			{ 
				localPathList.add(i); 
				printAllPathsUtil(i, d, isVisited, localPathList); 
				
				localPathList.remove(i); 
			} 
		} 
		
		isVisited[u] = false; 
	} 


	public Vector<String> AllPossiblities (int s , int d){
		printAllPaths(s, d);

		if (solutions.contains(""+s+"-"+d+"")){
			TheSolutions.add(""+s+"-"+d+"");
		}
		else {
			for (int i = 0 ;i<solutions.size() ; i++){
				TheSolutions.add(solutions.elementAt(i));
			}
		}


		Vector<String> TheRealSolutions = new Vector<String>();
		for (int i = 0 ; i<TheSolutions.size(); i++){
			String result = "";
		 	Vector<String> vector = extractWords(TheSolutions.elementAt(i));
			for (int j = 0 ; j<vector.size(); j++){
				boolean test = false ;
				for ( String key : ReseauMetro.keySet() ) {
					if (key.equals(vector.elementAt(j))){
						result = result+ReseauMetro.get(key)+"-";
						test = true ;
						break;

					}
				}
				if (!test){
					for ( String key : ReseauBus.keySet() ) {
						if (key.equals(vector.elementAt(j))){
							result = result+ReseauBus.get(key)+"-";
							test = true ;
							break;

						}
					}
				}
			}
			TheRealSolutions.add(result);
			result = "";
			for (int j = 0 ; j<vector.size(); j++){
				boolean test = false ;
				for ( String key : ReseauBus.keySet() ) {
					if (key.equals(vector.elementAt(j))){
						result = result+ReseauBus.get(key)+"-";
						test = true ;
						break;

					}
				}
				if (!test){
					for ( String key : ReseauMetro.keySet() ) {
						if (key.equals(vector.elementAt(j))){
							result = result+ReseauMetro.get(key)+"-";
							test = true ;
							break;

						}
					}
				}
			}
			TheRealSolutions.add(result);
		}
		return TheRealSolutions;
	}

	public Vector<String> extractWords(String str){

		String chaine = "";
		Vector v = new Vector();
		for (int i = 0 ; i< str.length() ; i++){
			if (str.charAt(i)!='-'){
				chaine = chaine + str.charAt(i);
			}
			else{
				v.add(chaine);
				chaine = "";
			}
			if(i==str.length()-1){
				v.add(chaine);
			}
		}

		Vector<String> SecondVector = new Vector<String>();
		for (int i = 0 ; i<v.size()-1; i++){
			SecondVector.add(v.elementAt(i)+"-"+v.elementAt(i+1));
		}


		return  SecondVector;
	}

}

// Villes  && index : 
// PASSAGE	0
// ariana	1
// ben arous	2
// manouba	3
// marsa	4
// sidi bousaid	5
// carthage	6
// jaafer	7
// aouina	8
// lac	9
// kram 	10
// la goulette	11
// borj louzir	12
// manzah 6	13
// manzah 5	14
// manzah 7	15
// manzah 8	16
// manzah 9	17
// manar	18
// bardo	19
// danden	20
// oued elil	21
// soukra	22
// khaznadar	23
// zahrouni	24
// cite etadhamen	25
// cite naser	26
// hay khadhra	27
// mornagia	28
// mourouj	29
// borj cedria	30
// rades	31
// megrin	32
// fouchana	33
// cite ghazela	34
// raoued	35
// daouar hicher	36
// tborba	37
// batan	38
// beb saadoun	39
// kabaria	40
// yasminete	41
// hamam lif	42
// gammarth	43
// sidi dhrif	44
// ibn khaldoun 45
// BARCELONE 46
// TGM 47