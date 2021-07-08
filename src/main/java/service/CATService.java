package service;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import utils.Constants;

public class CATService implements MqttCallbackExtended{
	
	static ArrayList<Double> temperatureValue;	
	
	static int temperatureAverage;
	static int count=0;
	
	MqttClient client;
	MqttConnectOptions mqOptions;
	
	
	public static void main(String[] args) throws MqttException, InterruptedException {		
		temperatureValue = new ArrayList<Double>();
		
		
		CATService catService = new CATService();
		catService.run();
		
		
	}
	
	public void run() throws MqttException {
		client = new MqttClient(Constants.broker,"cat");
	    client.setCallback(this);
	    
	    mqOptions=new MqttConnectOptions();
	    mqOptions.setCleanSession(true);
	    
	    client.connect(mqOptions);      
	    System.out.println("CAT Service is ON!!!");
	    
	    client.subscribe(Constants.topicSensorTemperature); 
	}
	
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
		temperatureValue.add(Double.parseDouble(message.toString()));
		count+=Double.parseDouble(message.toString());
		
		if(temperatureValue.size()==2) {	
			temperatureAverage = count/2;
				

			MqttTopic topic1 = client.getTopic(Constants.topicName);   
		    
			
		    MqttMessage message1 = new MqttMessage(Double.toString(temperatureAverage).getBytes());
			message1.setQos(Constants.qos); 
			message1.setRetained(true);
			topic1.publish(message1);
			message1.clearPayload();
			
			count =0;
			temperatureValue.clear();
		
		}
		
		
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	public void connectComplete(boolean reconnect, String serverURI) {
		// TODO Auto-generated method stub
		
	}

}
