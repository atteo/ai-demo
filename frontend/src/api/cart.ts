import axios from 'axios'
import type { Cart } from '@/types/cart'

export const getCart = (cartId: string): Promise<Cart> =>
  axios.get<Cart>(`/api/cart/${cartId}`).then(r => r.data)

export const addItem = (cartId: string, itemId: string, quantity: number): Promise<Cart> =>
  axios.post<Cart>(`/api/cart/${cartId}/items`, { itemId, quantity }).then(r => r.data)

export const updateQuantity = (cartId: string, itemId: string, quantity: number): Promise<Cart> =>
  axios.put<Cart>(`/api/cart/${cartId}/items/${itemId}`, { quantity }).then(r => r.data)

export const removeItem = (cartId: string, itemId: string): Promise<Cart> =>
  axios.delete<Cart>(`/api/cart/${cartId}/items/${itemId}`).then(r => r.data)
