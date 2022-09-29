import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
  Modal,
  TextArea,
} from 'antd';
import React, { useState, useEffect } from 'react';
import outboundApi from '@/services/spark/outboundApi';

const OutboundCreateModal = (props) => {
  const [confirmLoading, setConfirmLoading] = useState(false);

  const [allOutboundType, setAllOutboundType] = useState([]);

  const [selectOutboundType, setSelectOutboundType] = useState();

  const [form] = Form.useForm();

  const handleOk = () => {
    form.submit();
  };

  const handleCancel = () => {
    props.closeCreateModal();
  };

  useEffect(() => {
    outboundApi.getAllOutboundType().then((data) => {
      setAllOutboundType(data);
    });
  }, []);

  const doCreateNew = (values) => {
    setConfirmLoading(true);

    outboundApi.createNew({ ...values }).then((data) => {
      if (data) {
        message.success('operate successfully!');
        props.closeCreateModal();
      }
      setConfirmLoading(false);
    });
  };

  return (
    <>
      <Modal
        title="Create New OutBound"
        //open={open}
        visible={true}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            layout="vertical"
            onFinish={doCreateNew}
            labelAlign="right"
          >
            <Form.Item
              name="name"
              label="Name (unique)"
              rules={[{ required: true, message: 'Missing  Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="type"
              label="Outbound Type"
              rules={[{ required: true, message: 'Missing Outbound Type' }]}
            >
              <Select
                options={[
                  { label: 'JDBC', value: 'JDBC' },
                  { label: 'Kafka', value: 'KAFKA' },
                  ...allOutboundType,
                ]}
                style={{ width: 400 }}
                onChange={(value) => {
                  setSelectOutboundType(value);
                }}
              />
            </Form.Item>

            <Form.Item
              name="server"
              label="Host Name"
              rules={[{ required: true, message: 'Missing Host Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="port"
              label="Port"
              rules={[{ required: true, message: 'Missing Port' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="userName"
              label="User Name"
              rules={[{ required: true, message: 'Missing User Name' }]}
            >
              <Input style={{ width: 400 }} />
            </Form.Item>

            <Form.Item
              name="password"
              label="Password"
              rules={[{ required: true, message: 'Missing Password' }]}
            >
              <Input.Password style={{ width: 400 }} />
            </Form.Item>

            {selectOutboundType === 'JDBC' ? (
              <Form.Item
                name="jdbcType"
                label="JDBC Type"
                rules={[{ required: true, message: 'Missing JDBC Type' }]}
              >
                <Select
                  options={[
                    { label: 'Oracle', value: 'Oracle' },
                    { label: 'Mysql', value: 'Mysql' },
                  ]}
                  style={{ width: 400 }}
                />
              </Form.Item>
            ) : (
              ''
            )}

            {selectOutboundType === 'KAFKA' ? (
              <Form.Item
                name="topic"
                label="Topic Name"
                rules={[{ required: true, message: 'Missing Topic Name' }]}
              >
                <Input style={{ width: 400 }} />
              </Form.Item>
            ) : (
              ''
            )}

            <Form.Item name="properties" label="Properties">
              <Input.TextArea
                style={{ width: 400, height: 200 }}
                showCount
                placeholder="input other properties"
              />
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default OutboundCreateModal;
