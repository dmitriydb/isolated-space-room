import React, {Component} from "react";
import "./Radar.css";
import FlipMove from "react-flip-move";

class Radar extends Component {

    render() {

        let textStyle = {
            position: 'absolute',
            float: 'right',
            marginLeft: '325px',
            marginTop: '110px',
        }

        let text = this.props.temperature > 0 ? "+ " + this.props.temperature : "- " + -this.props.temperature;
        let currentTemperature = this.props.temperature;
        let temperatureColor;
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
        textStyle.color = temperatureColor;
        return (
            <div className="radarDiv">
                 <FlipMove duration={5} easing="ease-out">
                 <p style={textStyle}>{text}</p>
                 </FlipMove>
                 <img src="img/radar.png"></img>  
            </div>
        )
    }

}

export default Radar;