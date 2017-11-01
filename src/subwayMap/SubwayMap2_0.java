package subwayMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SubwayMap2_0 {
	//һ��281��վ
	private class Station{
		String stationName;
		boolean visited;
		
		//���������������ڵϽ�˹�����㷨
		int dist;
		Station path;
		
		public Station(String stationName) {
			super();
			this.stationName = stationName;
			this.visited = false;
			this.dist = Integer.MAX_VALUE;//������������
			this.path = null;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Station other = (Station) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (stationName == null) {
				if (other.stationName != null)
					return false;
			} else if (!stationName.equals(other.stationName))
				return false;
			return true;
		}

		public String toString() {
			return "Station [stationName=" + stationName + "]";
		}

		private SubwayMap2_0 getOuterType() {
			return SubwayMap2_0.this;
		}
	}
	
	private class Edge{
		//String name;
		Station station;
		String lineName;
		int distance;
		
		public Edge(Station station, String lineName, int distance) {
			super();
			this.station = station;
			this.lineName = lineName;
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "Edge [station=" + station + ", lineName=" + lineName + ", distance=" + distance + "]";
		}
	}
	
	private Map<Station,List<Edge>> map = new HashMap<>();
	
	/*
	 * ���캯�� 
	 * 		ֱ�ӵ���fileReader������ȡ�ļ�����ʼ��
	 * 		�˴��ı�����ͼ��2016??���  ���Բ��Ǻ�ȫ һ��18���� 281��վ
	 */
	public SubwayMap2_0(){
		fileReader();
	}
	
	/*
	 * ��ʼ����·ͼ
	 * 		����ͼ��һ����Ȩ˫��ͼ(������Ȩ����ͼ����)
	 * 		��������վ�ڵ����ڱ�ʱҪ������վ�ڵ㶼��¼�˱���Ϣ
	 * 		(�о�д���е㸴�� ����Ч���ﵽ��)
	 */
	private void fileReader(){
		File f = new File("mapdata/subwayInfo_bj.txt");
		if(f.exists()){
			
			try {
				FileReader out = new FileReader(f);
				BufferedReader br = new BufferedReader(out);
				
				String line = null;//���ڰ��ж�ȡ�ļ�
				int nums = Integer.parseInt(br.readLine());//��¼һ��������������
				
				for(int i = 0;i < nums;i++){
					br.readLine();//�հ���(�ļ��� ÿ����·֮���пհ��м�� ����Ҫ����)
					
					String trackInfo = br.readLine();//��·��Ϣ��(ÿ����·��һ�м�¼��·����վ�� �հ׷��ָ�)
					String[] track = trackInfo.split("[\\s]");
					String trackName = track[0];//��¼��ǰ��·��
					int trackStationsNum = Integer.parseInt(track[1]);//��¼��ǰ��·��վ��
					
					//System.out.println(trackName + "+" +trackStationsNum);
					
					String[] station;//��¼��ǰվ��Ϣ������ ��ֳ�������������
					String stationName;//��¼��ǰվ��վ��
					int nextDistance;//��¼��ǰվ����һվ�ľ���
					
					Station head = null;//��¼ʼ��վ
					Station preSta = null;//��¼��ǰվ����һվ
					Edge pre = null;//��¼��һվ����ǰվ��Ϣ�ı߽ڵ�
					
					Station s;//��¼��ǰվ
					
					for (int j = 0; j < trackStationsNum; j++) {
						line = br.readLine();//һ��վ��Ϣ
						
						station = line.split("[\\s]");//��ȡ��ǰվ��Ϣ �����
						stationName = station[0];
						nextDistance = Integer.parseInt(station[1]);
						
						s = new Station(stationName);
						
						//һ ����ǰվ�Լ���һվ����Ϣ��ȫ
						//1 ����ǰվ�洢��map(ֻ��map��û�д�վʱ�ż��� ����ֻ�����Ʊ���Ϣ)
						if(!map.containsKey(s)){
							map.put(s, new ArrayList<Edge>());
						}else{
							//****map���е�ǰվ����keyʱ  Ҫ��֤֮���½���Edgeָ��ͬһ��key
							//���д�keyset���ó�����ڲ�key���Ƹ�s �Ա�����ʹ��
							//PS:��Ϊ������� ����һ����bug(���Ǹо��ṹ��Ƶ��е�����  ���ǲ������)
							for (Station sta : map.keySet()) {
								if(s.stationName.equals(sta.stationName)){
									s = sta;
									break;
								}
							}
							
						}
						
						//2 ��ǰվ��ʼ��վʱ(����ʼ��վ ��ʱû�б���Ϣ)
						if(pre != null)
							map.get(s).add(pre);
						
						//3 Ϊ��ǰվ����һվ  �洢���ﱾվ �ı���Ϣ(��ȫ��һվ�ı���Ϣ)
						if(preSta != null)
							map.get(preSta).add(new Edge(s,trackName,pre.distance));
						
						//�� Ϊ��һվ�ļ�����׼��
						//1 ����ǰվ��Ϊ��һվ��ǰһվ�洢����(���Ĳ������ƿ�)
						preSta = s;
						//2 �½���ǰվ����һվ�ı���Ϣ  ��pre�洢
						pre = new Edge(s,trackName, nextDistance);
						
						//�� ��������վ�ڵ�
						//1 ʼ��վ����
						if(j == 0){
							head = s;
						}
						
						//2 �յ�վ����(��·���� �ǻ�·������)
						if(j == trackStationsNum-1 && nextDistance > 0){
							//��·Ҫ��ʼ��վ �洢�����յ�վ�ı���Ϣ
							map.get(head).add(pre);
							//���յ�վ�洢ʼ��վ�ı���Ϣ
							map.get(s).add(new Edge(head,trackName,nextDistance));
						}
						
						//System.out.println(stationName + "->" + nextDistance);
					}
					
				}
				br.close();
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	//private int count = 0; //��¼һ�������˶��ٸ�վ
	
	/*
	 * ������ȱ�����ͼ(����ûɶ�ð� ����ϰд��)
	 * 		���ݸ�����ĳ��վ��������(ǰ������������վ��)
	 * 		�߽ڵ㱻���޸���(��dfsӦ��������)
	 */
	public void dfs(String name){
		Station s = new Station(name);
		if(!map.containsKey(s)){
			System.out.println("��ͼ��û�����վ");
		}else {
			dfs(s);
		}
	}
	
	private void dfs(Station s){
		
		Set<Station> set = map.keySet();
		Iterator<Station> iterator = set.iterator();
		Station station;
		
		//���ⲿ����key��Ѱ�ҵ�map��������key
		while(iterator.hasNext()){
			station = iterator.next();
			if(station.stationName.equals(s.stationName)){
				s = station;
				break;
			}
		}
		
		//��key(վ�ڵ���з���)
		if(!s.visited){
			s.visited = true;
			System.out.print(s.stationName + "->");
			//count++;
		}
		
		//������վ�� ��ѯ����valueֵ(�ڽӱ�)
		List<Edge> list = map.get(s);
		for (int i = 0; i <list.size(); i++) {
			Edge e = list.get(i);
//			if(!"4��/������".equals(e.lineName)){
//				continue;
//			}//����������һ��ֻѡ��ĳ�����ߵķ�ʽ  �ȸ����� �п�д
			
			//���ﻹ��ʹ���ⲿkey��Ѱ����ʵ��key
			Station s2 = new Station(e.station.stationName);
			
			//ע��������Ҫ�½�һ�������� ����ʹ�������iterator
			//���������һ�ε�������  ���������µ���
			Iterator<Station> iterator2 = set.iterator();
			while(iterator2.hasNext()){
				station = iterator2.next();
				if(station.stationName.equals(s2.stationName) && !station.visited){
					//�ҵ�δ�����ʵ�վ�ڵ�  �ݹ����dfs
					dfs(station);
				}
			}
		}
	}
	
	public void shortestPath(String s1,String s2){
		
		Station sta1 = new Station(s1);
		Station sta2 = new Station(s2);
		if(!map.containsKey(sta1))
			System.out.println("�����ڵ����");
		if(!map.containsKey(sta2))
			System.out.println("�����ڵ�Ŀ�ĵ�");
		if(s1.equals(s2))
			System.out.println("�����Ŀ�ĵ��غ�,�߹�ȥ��");
		
		for (Station s : map.keySet()) {
			if(s.stationName.equals(s1))
				sta1 = s;
			if(s.stationName.equals(s2))
				sta2 = s;
		}
		dijkstraTravel(sta1);
		
		shortestPath(sta2);
		
		System.out.println("\n" + "�ܾ���ԼΪ" + sta2.dist);
		
	}
	
	private void shortestPath(Station end){
		
		if(end.path != null){
			shortestPath(end.path);
			System.out.print(" -> ");
		}
		System.out.print(end.stationName);
	}
	
	/*
	 * ʹ�õϽ�˹�����㷨�����·��
	 * 		����δÿ��վ�ڵ����һ��dist��path  ��ʾ�����ľ����·��
	 */
	private void dijkstraTravel(Station s){
		Set<Station> set = map.keySet();
		//forѭ��ʹ����վ�ڵ㶼Ϊδ����״̬
		for (Station station : set) {
			
			station.visited = false;
			station.dist = Integer.MAX_VALUE;
		}
		
		s.dist = 0;//����������Ϊ0
		
		boolean flag = true;//����ѭ����ʼ
		while(flag){
			Station v = null;//��ʾ��ǰվ�ڵ�
			Station w;//��ʾ��ǰվ�ڵ���ڽӽڵ�
			
			//forѭ���ҳ�վ�ڵ���δ������  �Ҿ���(dist)��С��վ�ڵ�
			for (Station station : map.keySet()) {
				if(station.visited == true)
					continue;
				if(v == null || station.dist < v.dist)
					v = station;
			}
			
			//���ʵ�ǰ�ڵ�
			v.visited = true;
			
			//������ǰ�ڵ���ڽӽڵ�
			List<Edge> list = map.get(v);
			for (int i = 0; i < list.size(); i++) {
				w = list.get(i).station;
				if(!w.visited){
					int d = list.get(i).distance;
					
					//�޸��ڽӽڵ�ľ����·��
					if(v.dist + d < w.dist){
						w.dist = v.dist + d;
						w.path = v;
					}
				}
			}
			
			//�����ڵ㼯  ��δ�����Ľڵ���whileѭ������
			Iterator<Station> iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				if(!iterator.next().visited){
					flag = true;
					break;
				}
			}
			if(!iterator.hasNext())
				flag = false;
		}
	}
	
//	public Station getS(String s){
//		return new Station(s);
//	}
	
	public static void main(String[] args) {
		
		SubwayMap2_0 m = new SubwayMap2_0();
		
		Scanner s = new Scanner(System.in);
		System.out.println("���������:");
		String start = s.next();
		System.out.println("�������յ�:");
		String end = s.next();
		System.out.println("���·������:");
		m.shortestPath(start, end);
	}
	
}
