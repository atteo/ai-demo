import { useEffect } from 'react'
import { Routes, Route } from 'react-router-dom'
import { AppShell } from '@mantine/core'
import ItemsListPage from '@/pages/ItemsListPage'
import ItemDetailPage from '@/pages/ItemDetailPage'
import CartPage from '@/pages/CartPage'
import AppHeader from '@/components/AppHeader'
import CartDrawer from '@/components/CartDrawer'
import { useCartStore } from '@/store/useCartStore'

function App() {
  const initCart = useCartStore(s => s.initCart)

  useEffect(() => {
    initCart()
  }, [initCart])

  return (
    <AppShell header={{ height: 60 }}>
      <AppShell.Header>
        <AppHeader />
      </AppShell.Header>
      <AppShell.Main>
        <Routes>
          <Route path="/" element={<ItemsListPage />} />
          <Route path="/items/:id" element={<ItemDetailPage />} />
          <Route path="/cart" element={<CartPage />} />
        </Routes>
      </AppShell.Main>
      <CartDrawer />
    </AppShell>
  )
}

export default App
