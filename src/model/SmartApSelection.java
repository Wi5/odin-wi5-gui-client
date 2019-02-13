package model;

public  class SmartApSelection {
	
	private int time_to_start;
	private int scanning_interval;
	private int added_time;
	private int signal_threshold;
	private int hysteresis_threshold;
	private double weight;
	private int pause;
	private String mode;
	private int txpowerSTA;
	private int thReqSTA;
	private String filename;

	public SmartApSelection(int time_to_start, int scanning_interval, int added_time, int signal_threshold, int hysteresis_threshold,
							double weight, int pause, String mode, int txpowerSTA, int thReqSTA, String filename ) {
			this.time_to_start = time_to_start;
			this.scanning_interval = scanning_interval;
			this.added_time =added_time;
			this.signal_threshold =signal_threshold;
			this.hysteresis_threshold =hysteresis_threshold;
			this.weight =weight;
			this.pause =pause;
			this.mode =mode;
			this.txpowerSTA =txpowerSTA;
			this.thReqSTA =thReqSTA;
			this.filename =filename;		
	}

	public int getTime_to_start() {
		return time_to_start;
	}

	public void setTime_to_start(int time_to_start) {
		this.time_to_start = time_to_start;
	}

	public int getScanning_interval() {
		return scanning_interval;
	}

	public void setScanning_interval(int scanning_interval) {
		this.scanning_interval = scanning_interval;
	}

	public int getAdded_time() {
		return added_time;
	}

	public void setAdded_time(int added_time) {
		this.added_time = added_time;
	}

	public int getSignal_threshold() {
		return signal_threshold;
	}

	public void setSignal_threshold(int signal_threshold) {
		this.signal_threshold = signal_threshold;
	}

	public int getHysteresis_threshold() {
		return hysteresis_threshold;
	}

	public void setHysteresis_threshold(int hysteresis_threshold) {
		this.hysteresis_threshold = hysteresis_threshold;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getPause() {
		return pause;
	}

	public void setPause(int pause) {
		this.pause = pause;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getTxpowerSTA() {
		return txpowerSTA;
	}

	public void setTxpowerSTA(int txpowerSTA) {
		this.txpowerSTA = txpowerSTA;
	}

	public int getThReqSTA() {
		return thReqSTA;
	}

	public void setThReqSTA(int thReqSTA) {
		this.thReqSTA = thReqSTA;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}

