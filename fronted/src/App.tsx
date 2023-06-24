import axios from "axios";


function subscription() {
  navigator.serviceWorker.ready.then((registration) => {
    registration.pushManager.getSubscription().then((subscription) => {
      if (subscription) {
        //DB에 저장
        axios
          .post("http://172.30.1.70:8080/push/subscribe", subscription)
          .then(response => {
            console.log(response);
          });
      } else {
        registration.pushManager
          .subscribe({
            userVisibleOnly: true,
            applicationServerKey:
              "BCt8g0mhCW9uQlP7V_A2fjz_AGybu6uQG5ZrxGuft6LU97bvfTAwiQNBXg_uNMnx6ss_szKuPDZs1ofRiYJKYa0",
          })
          .then((subscription) => {
            //DB에 저장
            axios
              .post("http:///172.30.1.70:8080/push/subscribe", subscription)
              .then((response) => {
                console.log(response);
              });
          });
      }
    });
  });
}

function App() {
  return <button onClick={() => subscription()}>구도구도구독</button>;
}
export default App;
