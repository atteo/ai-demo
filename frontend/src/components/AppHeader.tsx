import { Group, Title, Anchor, Indicator, ActionIcon } from '@mantine/core'
import { IconShoppingCart } from '@tabler/icons-react'
import { Link } from 'react-router-dom'
import { useCartStore } from '@/store/useCartStore'

export default function AppHeader() {
  const { cart, openDrawer } = useCartStore()
  const itemCount = cart?.items.reduce((sum, i) => sum + i.quantity, 0) ?? 0

  return (
    <Group h="100%" px="md" justify="space-between">
      <Anchor component={Link} to="/" underline="never">
        <Title order={3} c="blue">Generic Shop</Title>
      </Anchor>
      <Indicator label={itemCount} size={18} disabled={itemCount === 0} color="red">
        <ActionIcon variant="subtle" size="lg" onClick={openDrawer} aria-label="Open cart">
          <IconShoppingCart size={22} />
        </ActionIcon>
      </Indicator>
    </Group>
  )
}
