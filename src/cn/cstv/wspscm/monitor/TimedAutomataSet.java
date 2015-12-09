package cn.cstv.wspscm.monitor;
import java.util.ArrayList;
import java.util.List;


public class TimedAutomataSet {

		private int id;
		
		private List<TimedAutomata> timedAutomata = new ArrayList<TimedAutomata>();

		public TimedAutomataSet(){
			
		}
		
		public void SetMessageSet(TimedAutomataSet ms){
			this.timedAutomata = ms.timedAutomata;
		}
		
		public TimedAutomataSet(int id){
			this.id = id;
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		
		public void addTimedAutomata(TimedAutomata me) {
			if(this.timedAutomata!=null)this.timedAutomata.add(me);
			else timedAutomata.add(me);
		}

		public void clear()
		{
			this.timedAutomata.clear();
		}
		
		public List<TimedAutomata> getTimedAutomata() {
			return timedAutomata;
		}

		public void setTimedAutomata(List<TimedAutomata> timedAutomata) {
			this.timedAutomata = timedAutomata;
		}
	}
