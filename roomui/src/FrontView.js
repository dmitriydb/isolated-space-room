import React, {Component} from "react";
import "./FrontView.css";
import FlipMove from "react-flip-move";

class FrontView extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="frontViewDiv">
              <FlipMove duration={100} easing="ease-out">
                <img src = {this.props.img}></img>
                </FlipMove>

            </div>
        )
    }
}

export default FrontView;