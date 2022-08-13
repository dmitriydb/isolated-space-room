import React, {Component} from "react";

class SensorValue extends Component {

    constructor(props) {
        super(props);

    }

    render() {
        var barStyle = {
            width: 80 * (this.props.value / 100),
            backgroundColor: this.props.barColor,
            height: '7px',
            marginTop: 7
        };

        var divStyle = {
                width: '85px',
                borderBottom: '3px solid #3B6124',
                display: 'flex'
        }

        if (this.props.withoutBorder) {
            delete divStyle.borderBottom;
        }

        return (
            <div style={divStyle}>
                <div style={barStyle}></div>
            </div>
        )
    }

}

export default SensorValue;