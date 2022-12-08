exports = function(changeEvent) {
  const fullDocument = changeEvent.fullDocument;
  var alert = [];
  if(fullDocument.data.temp > 8){
    alert.push({"timestamp": new Date(), "message": `Temperature exceeded threshold for asset ${fullDocument.assetId}. Reported value is ${fullDocument.data.temp} °C.`});
  }
  if(fullDocument.data.doorOpened){
    alert.push({"timestamp": new Date(), "message": `Door is opened for asset ${fullDocument.assetId}.`});
  }
  if(alert.length > 0){
    const collection = context.services.get(<Cluster Name>).db("iot").collection("alerts");
    collection.insertMany(alert);
  }
