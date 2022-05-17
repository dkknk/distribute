import java.util.*;
enum State{UR,MR,US,MS};
enum Msg{NULL,PROPOSAL,MATCHED,ACCEPT};


class constant{
	private constant() {}
	public static final int maxV = 1000;
	public static final int maxP = 1000;

}

class Port{
	public int v;
	public int i;
	Port(int v,int i){
		this.v = v;
		this.i = i;
	}
}



class Node{
	public int node_num;
	public int stop_num;
	public int port_num;
	public State state;
	public Msg recv_Msg;
	public Msg[] port = new Msg[constant.maxV];
	public Set<Integer> X = new HashSet<Integer>();
	public Queue<Integer> M = new PriorityQueue<Integer>();

	public Node(int num,int port_num) {
		this.node_num = num;
		this.stop_num = -1;
		this.state = State.UR;
		this.port_num = port_num;
		for(int i=0;i<port_num;i++) {
			this.port[i] = Msg.NULL;
			this.X.add(i);
		}
	}
}


class ex3_6_1 {
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner sc = new Scanner(System.in);
		int V = sc.nextInt();
		int[] f = new int[V];
		for(int i=0;i<V;i++)
			f[i] = sc.nextInt(); 

		int[] P = new int[V];

		for(int i=0;i<V;i++)
			P[i] = sc.nextInt();

		Port[][] p = new Port[V][constant.maxP];

		for(int i=0;i<V;i++) {
			for(int j=0;j<P[i];j++) {
				int v_tmp = sc.nextInt();
				int i_tmp = sc.nextInt();
				p[i][j] = new Port(v_tmp-1,i_tmp-1);
			}
		}
		Node[] nodes = new Node[V];
		for(int i=0;i<V;i++) {
			nodes[i] = new Node(i,P[i]);
		}

		for(int loop_num=1;true;loop_num++) {
			boolean ok = true;
			for(int i=0;i<V;i++) {
				State s = nodes[i].state;
				if(s.equals(State.UR) || s.equals(State.MR)) {
					ok = false;
					break;
				}
			}
			if(ok) {
				System.out.println("The number of loop is " + loop_num + ".");
				break;
			}

			if(loop_num % 2 == 1) {//odd
				int k = loop_num / 2;

				for(int v=0;v<V;v++) {
					State s = nodes[v].state;

					if(f[v] == 1) { //white
						if(s.equals(State.UR)) {
							if(k < P[v]) {
								Port port_recv = p[v][k];
								nodes[port_recv.v].port[port_recv.i] = Msg.PROPOSAL;
							}
							if(k >= P[v]) {
								nodes[v].state = State.US;
							}
						}
						else if(s.equals(State.MR)) {
							for(int i=0;i<P[v];i++) {
								Port port_recv = p[v][i];
								nodes[port_recv.v].port[port_recv.i] = Msg.MATCHED;
							}
							nodes[v].state = State.MS;
						}
					}
				}

				for(int v=0;v<V;v++) {
					State s = nodes[v].state;

					if(f[v] == 2) { //black
						if(s.equals(State.UR)) {
							for(int i=0;i<P[v];i++) {
								if(nodes[v].port[i].equals(Msg.MATCHED)) {
									nodes[v].X.remove(i);
								}
								if(nodes[v].port[i].equals(Msg.PROPOSAL)) {
									nodes[v].M.add(i);
								}
							}

						}
					}

				}
			}
			if(loop_num % 2 == 0) {//even
				for(int v=0;v<V;v++) {
					State s = nodes[v].state;

					if(f[v] == 2) { //black
						if(s.equals(State.UR)) {
							if(!nodes[v].M.isEmpty()) {
								int i = nodes[v].M.peek();
								Port port_recv = p[v][i];
								nodes[port_recv.v].port[port_recv.i] = Msg.ACCEPT;
								nodes[v].state = State.MS;
								nodes[v].stop_num = i;
							}
							if(nodes[v].X.isEmpty()) {
								nodes[v].state = State.US;
							}
						}
					}
				}
				for(int v=0;v<V;v++) {
					State s = nodes[v].state;

					if(f[v] == 1) { //while
						if(s.equals(State.UR)) {
							for(int i=0;i<P[v];i++) {
								if(nodes[v].port[i].equals(Msg.ACCEPT)) {
									nodes[v].state = State.MR;
									nodes[v].stop_num = i;
								}
							}
						}
					}

				}
			}
		}
		for(int v=0;v<V;v++) {
			if(nodes[v].state.equals(State.MS)) {
				int i =  nodes[v].stop_num;
				System.out.println((v+1) + " " + (i+1) + " and " + (p[v][i].v+1) + " " + (p[v][i].i+1));
			}
		}
		sc.close();
	}
}