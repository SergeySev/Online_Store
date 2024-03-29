import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { fetch_main_categories } from '../../requests/requests'
import { MainCategoryItemGallery } from '../'
import s from './MainCategoriesListGallery.module.css'

export function MainCategoriesListGallery() {

	const dispatch = useDispatch()
	const categories = useSelector(store => store.categories)

	useEffect(() => {
		dispatch(fetch_main_categories())
	}, [])

	return (
		<section className={s.main_categories}>
			<div className='container'>
				<ul className={s.categories_wrapper}>
					{categories.map(elem =>
						<MainCategoryItemGallery key={elem.id}  {...elem} />
					)}
				</ul>

			</div>
		</section>
	)
}