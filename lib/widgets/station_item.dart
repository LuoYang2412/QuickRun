import 'package:flutter/material.dart';
import 'package:quick_run_flutter/model/station_model.dart';

class StationItem extends StatelessWidget {
  StationItem(this.station);

  final Station station;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Expanded(
          child: Center(
            child: station.status == StationStatus.ING
                ? Icon(
                    Icons.location_on,
                    color: Colors.lightGreen,
                  )
                : null,
          ),
        ),
        Expanded(
          child: Container(
            margin: EdgeInsets.only(top: 2),
            child: Stack(
              alignment: AlignmentDirectional.center,
              fit: StackFit.loose,
              children: <Widget>[
                Container(
                  height: 12,
                  color: Colors.lightBlueAccent,
                ),
                _getStationIcon(station.status),
              ],
            ),
          ),
        ),
        Expanded(
          flex: 2,
          child: Container(
            margin: EdgeInsets.only(top: 2),
            child: Center(
              child: Text(
                station.name,
                maxLines: 3,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ),
        ),
      ],
    );
  }

  Icon _getStationIcon(StationStatus status) {
    var icon;
    switch (status) {
      case StationStatus.ED:
        icon = Icon(
          Icons.add_circle,
          color: Colors.lightGreen,
        );
        break;

      case StationStatus.ING:
        icon = Icon(
          Icons.add_circle,
          color: Colors.yellow,
        );
        break;
      case StationStatus.WILL:
        icon = Icon(
          Icons.add_circle,
        );
        break;
    }
    return icon;
  }
}
