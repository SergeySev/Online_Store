import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { Breadcrumbs, ProductsList, SortSelect } from '../../components';
import { sort_by_new } from '../../store/reducers/favoriteSlice';
import s from './FavoriteProductsPage.module.css'
import { useNavigate } from 'react-router-dom';

export function FavoriteProductsPage() {

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const products_list = useSelector(store => store.favorite.favorite_list);

	const breadcrumbsItems = [
		{ text: 'Home /', link: '/Online_Store' },
		{ text: 'Favorites', link: '/favorite' },
	];

	useEffect(() => {
		dispatch(sort_by_new());
	}, [])

	const total_amount = useSelector(store => store.favorite.total_amount)
	if (!total_amount) {
		navigate('/Online_Store/#home')
	}

	return (
		<section className={s.favorite_page}>
			<div className="container">
				<Breadcrumbs items={breadcrumbsItems} />
				<h1 className='title'>Favorites</h1>
				<div className={s.favorite_top}>
					<div className={s.total}>
						<p className={s.items_count}>{total_amount}</p>
						<p>goods</p>
					</div>
					<SortSelect content='favorite' />
				</div>
				<ProductsList products={products_list} />
			</div>
		</section >
	)
}
