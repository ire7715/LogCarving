package logCarving;

import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticLog extends Log{
	
	private String host = null;
	private String cluster_name = null;
	private int apiport = 0;
	
	private String index = null;
	private String type = null;
	
	private Client client = null;

	@Override
	public void finalize() {
		this.client.close();
		this.client = null;
	}

	@Override
	public void close() {
		this.client.close();
		this.client = null;
	}

	@Override
	public void establishConnection(Map<String, String> info) throws Exception {
		this.host = info.get("host");
		this.index = info.get("index");
		this.type = info.get("type");
		this.apiport = Integer.parseInt(info.get("apiport"));
		this.cluster_name = info.get("cluster_name");
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", this.cluster_name)
				.put("client.transport.ping_timeout","5s")
				.put("client.transport.nodes_sampler_interval","5s")
				.put("client.transport.sniff",false)
				.build();
		this.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(this.host, this.apiport));
	}

	@Override
	public void send(int messageLabel, String message) throws Exception {
		int i;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
			sb.append("\"level\": \"" + messageLabel + "\",");
			sb.append("\"type\": \"" + "Custome message" + "\",");
			sb.append("\"message\": \"" + message + "\",");
			sb.append("\"stack\": [");
				for(i = 0; i < stack.length; i++){
					if(i != 0)
						sb.append(",");
					sb.append("\"" + stack[i].toString() + "\"");
				}
			sb.append("]");
		sb.append("}");
		
		IndexRequest iR = new IndexRequest(this.index, this.type);
		iR.source(sb.toString());
		this.client.index(iR).actionGet();
	}
	
	public void send(int messageLabel, Exception e) throws Exception {
		int i;
		StackTraceElement[] stack = e.getStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
			sb.append("\"level\": \"" + messageLabel + "\",");
			sb.append("\"type\": \"" + e.toString() + "\",");
			sb.append("\"message\": \"" + e.getMessage() + "\",");
			sb.append("\"stack\": [");
				for(i = 0; i < stack.length; i++){
					if(i != 0)
						sb.append(",");
					sb.append("\"" + stack[i].toString() + "\"");
				}
			sb.append("]");
		sb.append("}");
		
		IndexRequest iR = new IndexRequest(this.index, this.type);
		iR.source(sb.toString());
		client.index(iR).actionGet();
	}

}
