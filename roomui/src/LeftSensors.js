import React, {Component} from "react";
import "./LeftSensors.css";
import Sensor from "./Sensor";

class LeftSensors extends Component {

    render() {
        let temperatureColor;
        let currentTemperature = this.props.stats.temperature;
        if (currentTemperature > 100) {
            currentTemperature = 100;
        }
        if (currentTemperature < - 100) {
            currentTemperature = -100;
        }

        if (currentTemperature >= 75) {
            temperatureColor = 'red';
        } else if (currentTemperature >= 50) {
            temperatureColor = 'orangered';
        } else if (currentTemperature >= 25) {
            temperatureColor = 'orange';
        } else if (currentTemperature >= 0) {
            temperatureColor = 'greenyellow';
        } else if (currentTemperature <= -75) {
            temperatureColor = 'MidnightBlue';
        } else if (currentTemperature <= -50) {
            temperatureColor = 'MediumBlue';
        } else if (currentTemperature <= -25) {
            temperatureColor = 'LightSeaGreen';
        } else if (currentTemperature < 0) {
            temperatureColor = 'LightSkyBlue';
        }

        if (currentTemperature < 0) {
            currentTemperature = -currentTemperature;
        }
        let roomStateValue = this.props.stats.roomState == 'open' ? 0 : 100;
        return (
            <div className="leftSensorsDiv">
                    <Sensor value={100} barColor='#874BB2' label='FS' textColor='white'></Sensor>
                    <Sensor value={100} barColor='#874BB2' label='AS' textColor='white'></Sensor>
                    <Sensor value={roomStateValue} barColor='#B22222' label='LK' textColor='#7DD0DE'></Sensor>
                    <Sensor value={currentTemperature} barColor={temperatureColor} label='CT' textColor='#7DD0DE'></Sensor>
                    <Sensor value={this.props.stats.speed * 10} barColor='#874BB2' label='TC' textColor='#7DD0DE'></Sensor>
                    <Sensor value={66} withoutBorder='true' barColor='#AFEA86' label='AL' textColor='#7DD0DE'></Sensor>
            </div>
        )
    }

}

export default LeftSensors;