import { Box, Slider } from '@mui/material'
import React, { useState } from 'react'
import s from './FilterRange.module.css'
import './style.css'

export default function FilterPriceRange({ max, min, setRequestBody }) {

	const [value, setValue] = useState([0, 500]);

	const sliderChange = (event, newValue) => {
		setValue(newValue);
	}

	const get_range_data = (event) => {
		setRequestBody('Price', value)
	}

	return (
		<Box sx={{ width: 200 }}>
			<Slider
				value={value}
				onChange={sliderChange}
				onMouseUp={get_range_data}
				valueLabelDisplay="auto"
				step={10}
				min={min} max={max}
				className='mui_slider'
			/>
		</Box>
	)
}
