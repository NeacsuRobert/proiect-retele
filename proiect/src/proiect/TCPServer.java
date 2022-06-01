package proiect;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TCPServer extends Thread{
	
	private static Map<String,List<Produs>> produse = new HashMap<>();
	private Socket currentClient;
	private static List<Socket> clients = new Vector<>();
	
	public TCPServer(Socket currentClient) {
		super();
		this.currentClient = currentClient;
	}
	
	private String receive () throws Exception {
		InputStream inputStream = currentClient.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		String message = (String)objectInputStream.readObject();
		return message;
	}
	
	private void sendProduse() throws Exception{
		OutputStream outputStream = currentClient.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeUTF(produse.toString());
	}
	
	private void updateAllClients(Socket client, Produs produs) throws Exception {
		OutputStream outputStream = client.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeUTF(produs.toString());
	}
	
	private void send(String message) throws Exception {
        OutputStream outputStream = currentClient.getOutputStream();
        DataOutputStream dataOutputStream =
                new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(message);
	}
	
	@Override
	public void run() {
		try {
			String username = receive();
			if(produse.containsKey(username)) {
				send("Utilizatorul exista deja");
				throw new Exception("Utilizator existent");
			}
			else {
				produse.put(username, new Vector<Produs>());
				sendProduse();
				send("Vrei sa vinzi un produs?");
				String raspuns = receive();
				if(raspuns.equals("da")) {
					send("Numele produsului:");
					String numeProdus = receive();
					send("Pretul de pornire:");
					String pretPornire = receive();
					Produs prod = new Produs(numeProdus, Double.valueOf(pretPornire));
					List list = produse.get(username);
					list.add(prod);
					produse.put(username, list);
					for(Socket client: clients) {
						updateAllClients(client,prod);
					}
				}
				else {
					send("Vrei sa cumperi?");
					raspuns = receive();
					if(raspuns.equals("da")) {
						send("De la care utilizator ati dori sa cumparati?");
						raspuns = receive();
						List<Produs> listUser = produse.get(raspuns);
						send(listUser.toString());
						send("Introduceti numele produsului pentru care doriti sa licitati");
						raspuns = receive();
						Produs produsLicitat = new Produs();
						for(Produs produs : listUser) {
							if(produs.getNumeProdus().equals(raspuns)) {
								produsLicitat=produs;
							}
						}
						send("Ce suma vrei sa licitezi?");
						Double suma = Double.valueOf(receive());
						produsLicitat.setPretMaxim(suma);
						sendProduse();
					}
					
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			clients.remove(currentClient);
		}
	}
	
	public static void main(String[] args) {
        int port = 8000;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                TCPServer serverInstance = new TCPServer(clientSocket);
                serverInstance.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
