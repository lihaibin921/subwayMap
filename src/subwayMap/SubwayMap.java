package subwayMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/*
 * 算法未实现
 * 这是最初的想法  中途就换成2.0了
 */
public class SubwayMap {

	private int size = 0;
	
	edgeNode[] list;
	
	// 邻接链节点 (链表节点)
	private class VertexNode {

		String name;// 相邻站名

		int position;

		int distance;// 距离

		VertexNode next;// 下一个相邻站

		public VertexNode(String name, int distance, VertexNode next) {
			super();
			this.name = name;
			for(int i = 0;i<list.length;i++){
				if(name.equals(list[i].stationName)){
					this.position = i;
					break;
				}
			}
			this.distance = distance;
			this.next = next;
		}

		public VertexNode() {
			super();
		}
	}

	// 站节点 (数组节点)
	private class edgeNode {

		String stationName;// 站名

		boolean visited;// 是否访问过此节点(遍历时使用)

		VertexNode next;// 下一个相邻站

		public edgeNode(String stationName, VertexNode next) {
			super();
			this.visited = false;
			this.stationName = stationName;
			this.next = next;
		}

		public edgeNode() {
			super();
			this.visited = false;
		}
	}

	

	/*public SubwayMap() {

		edgeNode v1 = new edgeNode("v1", null);
		edgeNode v2 = new edgeNode("v2", null);
		edgeNode v3 = new edgeNode("v3", null);
		edgeNode v4 = new edgeNode("v4", null);
		edgeNode v5 = new edgeNode("v5", null);

		list = new edgeNode[] { v1, v2, v3, v4, v5 };
		
		VertexNode v1_2 = new VertexNode("v2", 1, null);
		VertexNode v1_4 = new VertexNode("v4", 4, null);
		v1_2.next = v1_4;

		VertexNode v2_1 = new VertexNode("v1", 1, null);
		VertexNode v2_3 = new VertexNode("v3", 2, null);
		VertexNode v2_5 = new VertexNode("v5", 6, null);
		v2_1.next = v2_3;
		v2_3.next = v2_5;

		VertexNode v3_2 = new VertexNode("v2", 2, null);
		VertexNode v3_4 = new VertexNode("v4", 3, null);
		VertexNode v3_5 = new VertexNode("v5", 5, null);
		v3_2.next = v3_4;
		v3_4.next = v3_5;

		VertexNode v4_1 = new VertexNode("v1", 4, null);
		VertexNode v4_3 = new VertexNode("v3", 3, null);
		v4_1.next = v4_3;

		VertexNode v5_2 = new VertexNode("v2", 6, null);
		VertexNode v5_3 = new VertexNode("v3", 5, null);
		v5_2.next = v5_3;

		v1.next = v1_2;
		v2.next = v2_1;
		v3.next = v3_2;
		v4.next = v4_1;
		v5.next = v5_2;

	}*/

	public void dfs(SubwayMap m, edgeNode v) {

		if (!v.visited) {
			v.visited = true;
			System.out.print(v.stationName + "\0");
		}
		VertexNode w;
		for (w = v.next;w != null; w = w.next) {
			if(!list[w.position].visited)
				dfs(m , list[w.position]);
		}
		

	}
	
	public void fileReader(){
		
		File f = new File("mapdata/subwayInfo_bj.txt");
		if(f.exists()){
			try {
				FileReader out = new FileReader(f);
				char[] c = new char[(int)f.length()];
				int i = out.read(c);
				for (int j = 0; j < c.length; j++) {
					System.out.print(c[j]);
				}

				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void fileReader2(){
		File f = new File("mapdata/subwayInfo_bj.txt");
		if(f.exists()){
			
			try {
				FileReader out = new FileReader(f);
				BufferedReader br = new BufferedReader(out);
				
				String line = null;
				int nums = Integer.parseInt(br.readLine());
				
				for(int i = 0;i < nums;i++){
					System.out.println(br.readLine());//空白行
					String trackInfo = br.readLine();//线路信息行
					String[] track = trackInfo.split("[\\s]");
					String trackName = track[0];
					int trackStationsNum = Integer.parseInt(track[1]);
					System.out.println(trackName + "+" +trackStationsNum);
					
					String stationName;
					int nextDistance;
					
					for (int j = 0; j < trackStationsNum; j++) {
						line = br.readLine();//一行站信息
						String[] station = line.split("[\\s]");
						stationName = station[0];
						nextDistance = Integer.parseInt(station[1]);
						
						System.out.println(stationName + "->" + nextDistance);
					}
					
				}
				br.close();
				out.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public static void main(String[] args) {

		 SubwayMap m = new SubwayMap();
		 //m.dfs(m, m.list[2]);
		 m.fileReader2();
	}

}
