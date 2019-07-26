class Station {
  String id;
  String name;
  StationStatus status;

  Station(
    this.id,
    this.name,
    this.status,
  );
}

enum StationStatus {
  ED,
  ING,
  WILL,
}
