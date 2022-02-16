package com.joor.streamsocketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//this class connects as a client to stream socket
public class ClientActivity extends AppCompatActivity {

    //variables for graphics
    Button buttonSendChat;
    EditText editTextChatBox, editTextMyMessage;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initGraphicElements();
        //get server and port values from intent
        Intent intent = getIntent();
        String server = intent.getStringExtra("server_address");
        String portString = intent.getStringExtra("port_number");
        //parse port int
        int port = Integer.parseInt(portString);

        //setup client
        client = new Client(server, port);
        //try to start client
        try {
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //init button andedittext fields and set onclick method to button
    private void initGraphicElements(){
        //initialize button and edittext fields
        buttonSendChat = findViewById(R.id.buttonSendChat);
        editTextChatBox = findViewById(R.id.editTextChatBox);
        editTextMyMessage = findViewById(R.id.editTextMyMessage);

        //fix so keyboard is hidden when user clicks outside of it
        View.OnFocusChangeListener ofcListener = new MyOnFocusChangeListener();
        editTextMyMessage.setOnFocusChangeListener(ofcListener);

        //add onclick listener to button
        buttonSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMyMessage.getText().toString().trim();
                if(message.isEmpty()){
                    Toast.makeText(ClientActivity.this, "Type a message before hitting send!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //send message to server
                    client.sendMessageToServer(message);
                    //update chat with your new message
                    updateChat("You: " + message);
                    //clear message box for user
                    editTextMyMessage.setText("");
                }
            }
        });
    }

    //update chat box with new message
    private void updateChat(String message){
        if(editTextChatBox.getText().toString().isEmpty()){
            editTextChatBox.setText(message);
        }else {
            editTextChatBox.setText(editTextChatBox.getText().toString() + "\n" + message);
        }
    }

    //onfocuschangelistener class that enables hiding of keyboard when focus is lost from edittext
    class MyOnFocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getId() == R.id.editTextMyMessage && !hasFocus){
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    //This class tries to connect to incoming server and port parameters. Multiple clients of this can connect.
    class Client {

        //variables for server and port
        String server;
        int port;
        Socket socket;
        PrintWriter out;
        String newMessage = "";

        public Client(String server, int port){
            this.server = server;
            this.port = port;
        }

        public void startClient() throws IOException{
            Thread clientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(server, port);

                        ServerConnection serverConnection = new ServerConnection(socket);
                        //for keyboard commands
                        BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
                        //for output (with auto-flush)
                        out = new PrintWriter(socket.getOutputStream(), true);

                        //start serverconnection thread
                        new Thread(serverConnection).start();
                        System.out.println("Connected to server via address: " + socket.getInetAddress() + ", and port: " + socket.getPort());

                        while (true) {
                            System.out.println("");
                            //send new message if client has sent one
                            if(!newMessage.equalsIgnoreCase("")){
                                out.println(newMessage);
                                newMessage = "";
                            }
                            //String command = key.readLine();

                            //break out of while loop if user wants to quit
                            //if (command.equals("quit")) break;

                            //out.println(command);
                        }
                        //socket.close();
                        //System.exit(0);
                    } catch(IOException e ) { e.printStackTrace(); }
                }
            });
            clientThread.start();
        }

        //send message to server method that is called when user sends via the app
        public void sendMessageToServer(String message){
            newMessage = message;
        }
    }

    //class for handling the server connection in separate thread
    class ServerConnection implements Runnable {

        private Socket serverSocket;
        private BufferedReader in;

        public ServerConnection(Socket socket) throws IOException {
            serverSocket = socket;
            //for input
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while(true) {
                    final String serverResponse = in.readLine();
                    //break if no response from server
                    if(serverResponse == null) break;
                    System.out.println("Server: " + serverResponse);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateChat("Server: " + serverResponse);
                        }
                    });

                }
            }catch (IOException e) { e.printStackTrace(); }
            finally {
                try {
                    in.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

}
