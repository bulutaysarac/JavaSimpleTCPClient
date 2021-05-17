
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextArea;

public class Client 
{
    private JTextArea messageBox;
    
    private int port;
    private String IPAddress;
    private boolean connected;
    
    private Socket clientSocket;
    
    private Runnable readTask;
    private Thread readThread;

    public Client(String IPAddress, int port, JTextArea messageBox) 
    {
        this.IPAddress = IPAddress;
        this.port = port;
        this.messageBox = messageBox;
    }

    public void connect() 
    {
        readTask = new Runnable()
        {
            @Override
            public void run()
            {
                try 
                {
                    clientSocket = new Socket(IPAddress, port);
                    messageBox.append("Connected!\n");
                    connected = true;
                    while (true)
                    {
                        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                        messageBox.append("Server : " + dis.readUTF() + "\n");
                    }
                }
                catch (Exception ex)
                {
                    messageBox.append(ex.getMessage());
                    connected = false;
                }
            }
        };
        
        readThread = new Thread(readTask);
        readThread.start();
    }
    
    public void disconnect()
    {
        if(readThread != null && readThread.isAlive())
        {
            try 
            {
                readThread.interrupt();
                messageBox.append("Disconnected!");
                clientSocket.close();
                connected = false;
            } 
            catch (IOException ex) 
            {
                messageBox.append(ex.getMessage());
            }
        }
    }

    public void writeData(String data) 
    {
        try 
        {
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
            dout.writeUTF(data);
            messageBox.append("You : " + data + "\n");
        }
        catch (Exception ex)
        {
            messageBox.append(ex.getMessage());
        }
    }

    public int getPort() 
    {
        return port;
    }

    public void setPort(int port) 
    {
        this.port = port;
    }

    public String getIPAddress() 
    {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) 
    {
        this.IPAddress = IPAddress;
    }

    public boolean isConnected() 
    {
        return connected;
    }

    public void setConnected(boolean connected) 
    {
        this.connected = connected;
    }
    
    public JTextArea getMessageBox() 
    {
        return messageBox;
    }

    public void setMessageBox(JTextArea messageBox) 
    {
        this.messageBox = messageBox;
    }
}
