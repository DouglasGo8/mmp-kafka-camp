const axios = require("axios");
const EventSource = require('eventsource');


// "recentchange" can be replaced with any valid stream
const url = 'https://stream.wikimedia.org/v2/stream/recentchange';
const eventSource = new EventSource(url);

eventSource.onopen = () => {
    console.info('Opened Connection');
}

eventSource.onerror = (event) => {
    console.error('Encountered error', event);
};

eventSource.onmessage = (event) => {
    // event.data will be a JSON string containing the message
    const data = JSON.parse(event.data);
    // Edits from the English Wikipedia
    if (data.wiki === "enwiki") {
        // Output the page title
        axios({
            method: 'post',
            url: "http://localhost:12080/wikimedia",
            data: {
                payload: data
            }
        });
        //console.log(data);
    }
};