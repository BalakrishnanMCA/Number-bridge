// ------------------------ DOM Elements ------------------------
const sendBtn = document.getElementById('sendBtn');
const input = document.getElementById('messageInput');
const chatMessages = document.getElementById('chatMessages');
const menuBtn = document.getElementById('menuBtn');
const menuDropdown = document.getElementById('menuDropdown');
const fileBtn = document.getElementById('fileBtn');
const fileInput = document.getElementById('fileInput');
const deleteBar = document.getElementById('deleteBar');
const deleteMeBtn = document.getElementById('deleteMeBtn');
const deleteEveryoneBtn = document.getElementById('deleteEveryoneBtn');
const blockBtn = document.getElementById('blockUser');
const mediaPreview = document.getElementById('mediaPreview');
const closePreview = document.getElementById('closePreview');
const previewContent = document.querySelector('.preview-content');

let selectedMessages = [];
let isBlocked = false; // true if user is blocked

// ------------------------ Timestamp ------------------------
function getCurrentTimestamp() {
    const now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12 || 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return `${hours}:${minutes} ${ampm}`;
}

// ------------------------ Message Functions ------------------------
function sendMessage(text, type="sent") {
    if(isBlocked) return;

    const msgDiv = document.createElement('div');
    msgDiv.className = 'message ' + type;

    const contentDiv = document.createElement('div');
    contentDiv.className = 'msg-content';

    if(type === 'received'){
        const avatarDiv = document.createElement('div');
        avatarDiv.className = 'msg-avatar';
        const avatarImg = document.createElement('img');
        avatarImg.src = '/css/avatar.png';
        avatarDiv.appendChild(avatarImg);
        msgDiv.appendChild(avatarDiv);
    }

    contentDiv.innerText = text;

    const timeSpan = document.createElement('span');
    timeSpan.className = 'timestamp';
    timeSpan.innerText = getCurrentTimestamp();
    contentDiv.appendChild(timeSpan);

    msgDiv.appendChild(contentDiv);
    msgDiv.addEventListener('click', () => toggleSelection(msgDiv));
    chatMessages.appendChild(msgDiv);
    addMediaEvents(msgDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// ------------------------ Selection ------------------------
function toggleSelection(msgDiv) {
    msgDiv.classList.toggle('selected');
    if(msgDiv.classList.contains('selected')){
        selectedMessages.push(msgDiv);
    } else {
        selectedMessages = selectedMessages.filter(m => m !== msgDiv);
    }
    updateDeleteBar();
}

function updateDeleteBar() {
    deleteBar.style.display = selectedMessages.length > 0 ? 'flex' : 'none';
}

deleteMeBtn.addEventListener('click', () => {
    selectedMessages.forEach(msg => msg.remove());
    selectedMessages = [];
    updateDeleteBar();
});

deleteEveryoneBtn.addEventListener('click', () => {
    selectedMessages.forEach(msg => {
        msg.remove();
    });
    selectedMessages = [];
    updateDeleteBar();
});

// ------------------------ Send Button & Enter ------------------------
sendBtn.addEventListener('click', () => {
    if(isBlocked){
        alert("You cannot send messages. User is blocked!");
        return;
    }
    const text = input.value.trim();
    if(text !== '') { 
        sendMessage(text); 
        input.value = ''; 
    }
});

input.addEventListener('keypress', (e) => {
    if(e.key === 'Enter'){
        e.preventDefault();
        sendBtn.click();
    }
});

// ------------------------ File Upload (+ button) ------------------------
fileBtn.addEventListener('click', () => {
    if(isBlocked){
        alert("You cannot send media. User is blocked!");
        return;
    }
    fileInput.click();
});

fileInput.addEventListener('change', (e) => {
    if(isBlocked){
        alert("You cannot send media. User is blocked!");
        fileInput.value = '';
        return;
    }
    const files = Array.from(e.target.files);

    files.forEach(file => {
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message sent';
        const contentDiv = document.createElement('div');
        contentDiv.className = 'msg-content';

        if(file.type.startsWith('image/')){
            const img = document.createElement('img');
            img.src = URL.createObjectURL(file);
            img.style.maxWidth = "200px";
            img.style.borderRadius = "10px";
            contentDiv.appendChild(img);
        } else if(file.type.startsWith('video/')){
            const vid = document.createElement('video');
            vid.src = URL.createObjectURL(file);
            vid.controls = true;
            vid.style.maxWidth = "200px";
            vid.style.borderRadius = "10px";
            contentDiv.appendChild(vid);
        } else {
            contentDiv.innerText = file.name;
        }

        const timeSpan = document.createElement('span');
        timeSpan.className = 'timestamp';
        timeSpan.innerText = getCurrentTimestamp();
        contentDiv.appendChild(timeSpan);

        msgDiv.appendChild(contentDiv);
        msgDiv.addEventListener('click', () => toggleSelection(msgDiv));

        chatMessages.appendChild(msgDiv);
        addMediaEvents(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    });

    fileInput.value = '';
});

// ------------------------ Media Preview ------------------------
function showPreview(media) {
    previewContent.innerHTML = '';
    if(media.tagName === 'IMG'){
        const img = document.createElement('img');
        img.src = media.src;
        previewContent.appendChild(img);
    } else if(media.tagName === 'VIDEO'){
        const vid = document.createElement('video');
        vid.src = media.src;
        vid.controls = true;
        previewContent.appendChild(vid);
    }
    mediaPreview.style.display = 'flex';
}

closePreview.addEventListener('click', () => {
    mediaPreview.style.display = 'none';
});

// Attach click/double-click events to media in messages
function addMediaEvents(msgDiv) {
    const media = msgDiv.querySelector('img, video');
    if(!media) return;

    media.addEventListener('click', () => showPreview(media));
    media.addEventListener('dblclick', () => toggleSelection(msgDiv));
}

// ------------------------ Three-Dot Menu ------------------------
menuBtn.addEventListener('click', () => {
    menuDropdown.classList.toggle('show');
    menuBtn.style.background = menuDropdown.classList.contains('show') ? '#285e8e' : '#357ABD';
});

document.addEventListener('click', function(event) {
    if (!menuBtn.contains(event.target) && !menuDropdown.contains(event.target)) {
        menuDropdown.classList.remove('show');
        menuBtn.style.background = '#357ABD';
    }
});

// ------------------------ Menu Actions ------------------------
blockBtn.addEventListener('click', () => {
    isBlocked = !isBlocked;
    if(isBlocked){
        alert("You have blocked this user. No messages can be sent.");
        blockBtn.innerText = "Unblock User";
        input.disabled = true;
        input.placeholder = "User is blocked...";
    } else {
        alert("User unblocked. You can now send messages.");
        blockBtn.innerText = "Block User";
        input.disabled = false;
        input.placeholder = "Type a message...";
    }
});

document.getElementById('clearChat').addEventListener('click', () => {
    chatMessages.innerHTML = '';
    selectedMessages = [];
    updateDeleteBar();
});

document.getElementById('callUser').addEventListener('click', ()=> alert("Calling..."));



// ------------------------ Initial Setup ------------------------
 let stompClient = null;
      const currentUser =
        document.querySelector(".chat-container").dataset.username;
      const contactUser =
        document.querySelector(".chat-container").dataset.contact;
      //const chatMessages = document.getElementById("chatMessages");
      const messageInput = document.getElementById("messageInput");
     // const sendBtn = document.getElementById("sendBtn");

      // Add message bubble
      function addMessage(content, type) {
        const msgDiv = document.createElement("div");
        msgDiv.classList.add("message", type); // sender or receiver
        msgDiv.innerText = content;
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
      }

      // Connect to WebSocket
      function connect() {
        const socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
          // Subscribe to receiver’s channel
          stompClient.subscribe(
            "/topic/private-" + senderName,
            function (message) {
              const msgObj = JSON.parse(message.body);
              addMessage(msgObj.content, "receiver");
            }
          );

          // Load chat history
          fetch(`/chat/history?user1=${senderName}&user2=${receiverName}`)
            .then((res) => res.json())
            .then((messages) => {
              messages.sort((a, b) => a.timestamp - b.timestamp);
              messages.forEach((msg) => {
                const type = msg.sender === senderName ? "sender" : "receiver";
                addMessage(msg.content, type);
              });
            });
        });
      }

      // Send message
      sendBtn.addEventListener("click", () => {
        const text = messageInput.value.trim();
        if (text && stompClient) {
          const message = {
            sender: contactUser,
            receiver: receiverName,
            content: text,
          };
          stompClient.send(
            "/app/chat.sendMessage",
            {},
            JSON.stringify(message)
          );
          addMessage(text, "sender");
          messageInput.value = "";
        }
      });

      // Connect on page load
      connect();
