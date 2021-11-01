"use strict";

//let can = document.getElementsByTagName( 'canvas' )[0];
//
//can.style = "border: 1px solid red; padding: 20px;";


//let ctx = can.getContext( '2d' );
//
//let input = document.createElement('textarea');
//input.value="name";
//console.log(input);
//ctx.fillText(input,500,500);

let message=document.getElementById('message');
let startbtn=document.getElementById('startChat');
let username_=document.getElementById('userName');
let roomname_=document.getElementById('textRoom');
let btTA=document.getElementById('btTA');
let TA=document.getElementById('TA');

let ws = new WebSocket("ws://localhost:8080");


startbtn.addEventListener("click",handlePress);

function handlePress(event){
    let username=(username_.value);
    let roomname=(roomname_.value);

    for(let i=0;i<roomname.length;i++){
        if(roomname[i]<'a' || roomname[i]>'z' || roomname[i]==' '){
            alert("Room name must be small with no space!");
            username_.value='';
            roomname_.value='';
            return;
        }
    }
    ws.send("join "  + roomname);
    console.log("connected!");

    let br = document.createElement("br");
    message.append("join "+roomname);
    message.append(br);
    connected();
}

function connected(){
    ws.onmessage = handleMessageCB;
    ws.onopen =handleConnectCB;
    ws.onclose = handleCloseCB;
    ws.onerror = handleWSErrorCB;
}

function handleMessageCB(event){
    let br = document.createElement("br");

    let msg=event.data;
    let text = JSON.parse(msg);
    let user=text.user;
    let usermsg=text.message;

    message.append(user+" "+usermsg);
    message.append(br);

    br.scrollIntoView();
    message.scrollTo(0,message.scrollHeight);
    console.log(user);
    console.log(usermsg);
    TA.value='';
}

function handleConnectCB(){
    console.log("connected!");
}


function handleCloseCB(){
    console.log("close!");
}

function handleWSErrorCB(){
    console.log("has error!");
}


btTA.addEventListener("click", handleClick);
TA.addEventListener("keypress", handleClick);

function handleClick(event){
    if(event.type=="click" || event.keyCode==13){
       let username=(username_.value);
       let message1=(TA.value);
       ws.send(username +" :"+ message1);
    }
}


//let ws = new WebSocket("ws://localhost:8080");










// let ws= new WebSocket("ws://localhost:8080");
//            ws.onopen =handleConnectCB;
//            ws.onmessage = handleMessageCB;
//            ws.onclose = handleCloseCB;
//            ws.onerror = handleWSErrorCB;
//
//
//            //ws.send(username + " " + msg);
//
//
//btTA.addEventListener("click", handleClick);
//
//function handleClick(event){
//            let ws= new WebSocket("ws://localhost:8080");
//            ws.onopen =handleConnectCB;
//            ws.onmessage = handleMessageCB;
//            ws.onclose = handleCloseCB;
//            ws.onerror = handleWSErrorCB;
//
//
//            //ws.send(username + " " + msg);
//
//            function handleMessageCB(event){
//                        console.log(event.data);
//                        let msg=event.data;
//                        message.append(msg);
//                    }
//
//            function handleConnectCB(){
//                        console.log("connect");
//                        ws.send("join " + roomname.value);
//            }
//
//                    function handleCloseCB(){
//
//                    }
//
//                    function handleWSErrorCB(){
//
//                    }
//
//        }



