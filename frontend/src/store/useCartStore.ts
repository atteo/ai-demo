import { create } from 'zustand'
import type { Cart } from '@/types/cart'
import * as cartApi from '@/api/cart'

const CART_ID_KEY = 'cartId'

interface CartStore {
  cartId: string | null
  cart: Cart | null
  loading: boolean
  drawerOpen: boolean
  initCart: () => Promise<void>
  addItem: (itemId: string, quantity: number) => Promise<void>
  updateQuantity: (itemId: string, quantity: number) => Promise<void>
  removeItem: (itemId: string) => Promise<void>
  openDrawer: () => void
  closeDrawer: () => void
}

export const useCartStore = create<CartStore>((set, get) => ({
  cartId: null,
  cart: null,
  loading: false,
  drawerOpen: false,

  initCart: async () => {
    const stored = localStorage.getItem(CART_ID_KEY)
    if (!stored) return
    set({ cartId: stored, loading: true })
    try {
      const cart = await cartApi.getCart(stored)
      set({ cart })
    } catch {
      // cart not found on server — clear stale id
      localStorage.removeItem(CART_ID_KEY)
      set({ cartId: null })
    } finally {
      set({ loading: false })
    }
  },

  addItem: async (itemId, quantity) => {
    let { cartId } = get()
    if (!cartId) {
      cartId = crypto.randomUUID()
      localStorage.setItem(CART_ID_KEY, cartId)
      set({ cartId })
    }
    const cart = await cartApi.addItem(cartId!, itemId, quantity)
    set({ cart, drawerOpen: true })
  },

  updateQuantity: async (itemId, quantity) => {
    const { cartId } = get()
    if (!cartId) return
    const cart = await cartApi.updateQuantity(cartId!, itemId, quantity)
    set({ cart })
  },

  removeItem: async (itemId) => {
    const { cartId } = get()
    if (!cartId) return
    const cart = await cartApi.removeItem(cartId!, itemId)
    set({ cart })
  },

  openDrawer: () => set({ drawerOpen: true }),
  closeDrawer: () => set({ drawerOpen: false }),
}))
