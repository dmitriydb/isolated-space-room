import React, {Component} from "react";
import "./RightSensors.css";
import Sensor from "./Sensor";

class RightSensors extends Component {

    render() {
        let value1 = this.props.stats.vector == 1 ? Math.random() * 100: 0;
        let value2 = this.props.stats.vector == 0 ? Math.random() * 100: 0;

        return (
            <div className="rightSensorsDiv">
                    <Sensor rv='true' value={value1} barColor='#A0C837' label='1' textColor='white'></Sensor>
                    <Sensor rv='true' value={value2} barColor='#A0C837' label='2' textColor='white'></Sensor>
                    <Sensor rv='true' value={100 - value2} barColor='#A0C837' label='3' textColor='#7DD0DE'></Sensor>
                    <Sensor withoutBorder='true' rv='true' value={100 - value1} barColor='#A0C837' label='4' textColor='#7DD0DE'></Sensor>
                
            </div>
        )
    }

}

export default RightSensors;