export const establishSocketConnection = () => {
  const socket = new WebSocket('ws://localhost:8080/ws-excel');
  socket.onopen = () => {
    console.log('Connected to server');
  };
  socket.onmessage = (event) => {
    console.log('Message from server', event.data);
  };
  socket.onclose = () => {
    console.log('Disconnected from server');
  };
  socket.onerror = (error) => {
    console.log('Error', error);
  };
}