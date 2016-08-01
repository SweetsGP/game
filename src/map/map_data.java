package map;

public class map_data{

	/*map data*/
	public int[][] mp1 = {
			{1,2,1,1,1,1,1,1,1,2,1,1,1,1,1,1},
			{1,2,1,2,2,2,2,1,1,2,1,2,2,2,2,1},
			{1,1,1,2,1,1,2,1,1,1,1,2,1,1,2,1},
			{1,2,2,2,1,1,2,1,1,2,2,2,1,1,2,1},
			{1,1,1,1,1,1,2,1,1,1,1,1,1,1,2,1},
			{1,2,2,2,2,2,1,1,1,2,2,2,2,2,1,1},
			{1,2,1,1,2,1,1,1,1,2,1,1,1,1,1,1},
			{1,2,1,2,2,2,2,1,1,2,1,2,1,2,2,2},
			{1,1,1,2,1,1,2,1,1,1,1,2,1,1,1,1},
			{1,2,2,2,1,1,2,1,1,2,2,2,1,1,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,2,1,1,1,1},
			{1,2,2,2,2,2,1,1,1,2,2,2,1,1,1,1}
	};
	public int[][] mp2 = {
			{1,2,1,1,1,1,1,1,1,2,1,1,1,1,1,1},
			{1,2,2,1,2,2,2,1,1,2,2,1,2,2,2,1},
			{1,2,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
			{1,1,1,2,2,1,2,2,1,1,1,2,2,1,2,2},
			{2,1,2,2,2,2,2,2,2,1,2,2,2,2,2,2},
			{2,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1},
			{1,2,1,1,1,1,1,1,1,2,1,1,1,1,1,1},
			{1,2,2,1,2,2,2,1,1,2,2,1,2,2,2,1},
			{1,2,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
			{1,1,1,2,2,1,2,2,1,1,1,2,2,1,2,2},
			{2,1,2,2,2,2,2,2,2,1,2,2,2,2,2,2},
			{2,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1}
	};
	
	public int npc_units = 0;
	
	public int Max_enc = 30;
	
	/*enemy data*/
	
	public int mp1_enemy = 1;	//databese
	
	public int mp2_enemy = 1;	//database
	
	/*message data*/	//databese
	public int[] message_long = {0,4,2};
	
	public int[] action_long = {0,3,2};
	
	public int[] encounter_long = {0,2};
	
	public String[] message_text_1 = {" ","He is GORITYU ?","Immediately, His Adventure will begin !!","Come On YOU !!"};
	
	public String[] message_text_2 = {" ","Moving to the NEXT MAP"};
	
	public String[] action_text_1 = {" ","Check the under ground","There is grass"};
	
	public String[] action_n_text_1 = {" ","やっほー"};
	
	public String[] action_n_text_2 = {" ","げんき？"};
	
	public String[] encounter_text_1 = {" ","You encounter an enemy !"};
	
}

